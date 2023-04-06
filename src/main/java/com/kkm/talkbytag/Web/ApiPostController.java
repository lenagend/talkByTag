package com.kkm.talkbytag.Web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkm.talkbytag.domain.Comment;
import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.domain.UserInfo;
import com.kkm.talkbytag.dto.ImageUploadResponse;
import com.kkm.talkbytag.dto.PostWithUserInfo;
import com.kkm.talkbytag.service.PostService;
import com.kkm.talkbytag.service.UserInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiPostController {

    private final PostService postService;

    private final UserInfoService userInfoService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Value("${upload.path}")
    private String uploadPath;

    @Value("${avatar.placeholder.path}")
    private String avatarPlaceholderPath;

    public ApiPostController(PostService postService, UserInfoService userInfoService) {
        this.postService = postService;
        this.userInfoService = userInfoService;
    }

    @GetMapping("/posts")
    public Flux<PostWithUserInfo> getPosts(@RequestParam int offset, @RequestParam int limit){
        return postService.getPosts()
                .filter(Post::isPublished)
                .skip(offset)
                .take(limit)
                .concatMap(this::getPostWithUserInfo);
    }

    private Mono<PostWithUserInfo> getPostWithUserInfo(Post post) {
        return userInfoService.findByUsername(post.getUsername())
                .switchIfEmpty(Mono.defer(() -> {
                    UserInfo anonymousUserInfo = new UserInfo();
                    anonymousUserInfo.setNickname("익명");
                    anonymousUserInfo.setProfileImage(avatarPlaceholderPath);
                    return Mono.just(anonymousUserInfo);
                }))
                .flatMap(user -> {
                    PostWithUserInfo postWithUserInfo = new PostWithUserInfo();
                    postWithUserInfo.setId(post.getId());
                    postWithUserInfo.setHashTag(post.getHashTag());
                    postWithUserInfo.setUsername(post.getUsername());
                    postWithUserInfo.setContents(post.getContents());
                    postWithUserInfo.setCreatedAt(post.getCreatedAt());
                    postWithUserInfo.setModifiedAt(post.getModifiedAt());
                    postWithUserInfo.setLiked(0);
                    postWithUserInfo.setViewCount(0);
                    postWithUserInfo.setPublished(post.isPublished());
                    postWithUserInfo.setCommentCount(0L);
                    postWithUserInfo.setNickname(user.getNickname());
                    postWithUserInfo.setProfileImage(user.getProfileImage());

                    return postService.getCommentCount(post.getId(), post.isPublished())
                            .map(commentCount -> {
                                postWithUserInfo.setCommentCount(commentCount);
                                return postWithUserInfo;
                            });
                });
    }

    @GetMapping("/posts/read/{id}")
    public Mono<PostWithUserInfo> getPostById(@PathVariable String id){
        return postService.getPostByPostId(id)
                .flatMap(this::getPostWithUserInfo);
    }

    @GetMapping("/posts/search")
    public Flux<Post> searchByHashTag(@RequestParam("q") String q, @RequestParam int offset, @RequestParam int limit){
        boolean startsWithAtSign = q != null && !q.isEmpty() && q.charAt(0) == '@';
        if(startsWithAtSign){
            return postService.searchByHashTag(q)
                    .filter(Post::isPublished)
                    .skip(offset)
                    .take(limit);
        }else{
            return postService.searchByContents(q)
                    .filter(Post::isPublished)
                    .skip(offset)
                    .take(limit);
        }
    }

    @PostMapping("/posts")
    public Mono<Post> createPost(@RequestBody Post post){
        return postService.savePost(post);
    }

    @PutMapping("/posts/{postId}")
    public Mono<Post> updatePost(@PathVariable String postId, @RequestBody Post post){
        return postService.getPostByPostId(postId)
                .flatMap(p -> {
                    Optional.ofNullable(post.getContents()).ifPresent(p::setContents);
                    Optional.ofNullable(post.getUsername()).ifPresent(p::setUsername);
                    Optional.ofNullable(post.getHashTag()).ifPresent(p::setHashTag);
                    Optional.ofNullable(post.isPublished()).ifPresent(p::setPublished);

                    p.setModifiedAt(LocalDateTime.now());
                    return postService.savePost(p);
                });
    }

    @PostMapping("/comments")
    public Mono<Comment> createComment(@RequestBody Comment comment) {
        return postService.saveComment(comment);
    }

    @GetMapping("/{postId}/comments")
    public Flux<Comment> getCommentsByPostId(@PathVariable String postId){
        return this.postService.getCommentsByPostId(postId)
                .filter(Comment::isPublished);
    }

    @PutMapping("/comments/{id}")
    public Mono<Comment> updateComment(@PathVariable String id, @RequestBody Comment comment){
        return postService.getCommentById(id)
                .flatMap(c ->{
                    Optional.ofNullable(comment.getContents()).ifPresent(c::setContents);
                    Optional.ofNullable(comment.getUsername()).ifPresent(c::setUsername);
                    Optional.ofNullable(comment.isPublished()).ifPresent(c::setPublished);

                    c.setModifiedAt(LocalDateTime.now());
                    return postService.saveComment(c);
                });
    }

    @GetMapping("/comments/count/{postId}")
    public Mono<Long> getCommentCount(@PathVariable String postId){
        return postService.getCommentCount(postId, true);
    }

    @GetMapping("/{upperCommentId}/replies")
    public Flux<Comment> getCommentsByUpperCommentId(@PathVariable String upperCommentId){
        return this.postService.getCommentsByUpperCommentId(upperCommentId)
                .filter(Comment::isPublished);
    }


    // 이미지 업로드
    @RequestMapping(value = "/posts/upload-image", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<?>> uploadImage(@RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono
                .flatMap(filePart -> {
                    String fileName = UUID.randomUUID().toString();
                    Path path = Paths.get(uploadPath, fileName);
                    try {
                        Files.createDirectories(path.getParent());
                    } catch (IOException e) {
                        return Mono.error(e);
                    }
                    return filePart.transferTo(new File(path.toString()))
                            .thenReturn(new ImageUploadResponse("/images/" + fileName));
                })
                .map(response -> {
                    try {
                        return ResponseEntity.ok().body(objectMapper.writeValueAsString(response));
                    } catch (JsonProcessingException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

}
