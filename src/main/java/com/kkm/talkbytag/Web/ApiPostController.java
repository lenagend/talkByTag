package com.kkm.talkbytag.Web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkm.talkbytag.domain.Comment;
import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.dto.CommentWithUserInfo;
import com.kkm.talkbytag.dto.PostWithUserInfo;
import com.kkm.talkbytag.service.AuthenticationService;
import com.kkm.talkbytag.service.PostService;
import com.kkm.talkbytag.service.UserInfoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiPostController {

    private final PostService postService;
    private final UserInfoService userInfoService;
    private final AuthenticationService authenticationService;


    public ApiPostController(PostService postService, UserInfoService userInfoService, AuthenticationService authenticationService) {
        this.postService = postService;
        this.userInfoService = userInfoService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/posts")
    public Flux<PostWithUserInfo> getPosts(@RequestParam int offset, @RequestParam int limit){
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postService.getPosts(pageable, true)
                .concatMap(postService::getPostWithUserInfo);
    }

    @GetMapping("/posts/my")
    public Flux<PostWithUserInfo> getMyPosts(@RequestParam int offset, @RequestParam int limit, @RequestParam boolean published, @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        String username = authenticationService.extractUsername(token);
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postService.findPublishedPostsByUser(pageable, username, published)
                .concatMap(postService::getPostWithUserInfo);
    }

    @GetMapping("/posts/read/{id}")
    public Mono<PostWithUserInfo> getPostById(@PathVariable String id){
        return postService.getPostByPostId(id)
                .flatMap(postService::getPostWithUserInfo);
    }

    @GetMapping("/posts/search")
    public Flux<PostWithUserInfo> searchByHashTag(@RequestParam("q") String q, @RequestParam int offset, @RequestParam int limit) throws UnsupportedEncodingException {
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        String decodedQ = URLDecoder.decode(q, "UTF-8");
        boolean startsWithHashSign = decodedQ != null && !decodedQ.isEmpty() && decodedQ.charAt(0) == '#';
        if(startsWithHashSign){
            return postService.searchByHashTag(pageable, q, true)
                    .concatMap(postService::getPostWithUserInfo);
        }else{
            return postService.searchByContents(pageable, q, true)
                    .concatMap(postService::getPostWithUserInfo);
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
    public Flux<CommentWithUserInfo> getCommentsByPostId(@PathVariable String postId){
        return this.postService.getCommentsByPostId(postId)
                .filter(Comment::isPublished)
                .concatMap(postService::getCommentWithUserInfo);
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
    public Flux<CommentWithUserInfo> getCommentsByUpperCommentId(@PathVariable String upperCommentId){
        return this.postService.getCommentsByUpperCommentId(upperCommentId)
                .filter(Comment::isPublished)
                .concatMap(postService::getCommentWithUserInfo);
    }

    @PutMapping("/posts/{postId}/like")
    public Mono<ResponseEntity<Void>> togglePostLike(@PathVariable String postId, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String username = authenticationService.extractUsername(token);

            return postService.toggleLike(username, postId, null);
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    @PutMapping("/comments/{commentId}/like")
    public Mono<ResponseEntity<Void>> toggleCommentLike(@PathVariable String commentId, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String username = authenticationService.extractUsername(token);

            return postService.toggleLike(username, null, commentId);
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    @GetMapping("/posts/{postId}/liked")
    public Mono<ResponseEntity<Boolean>> isPostLikedByUser(@PathVariable String postId, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String username = authenticationService.extractUsername(token);

            return postService.isPostLikedByUser(postId, username)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.ok(false));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
        }
    }

    @GetMapping("/comments/{commentId}/liked")
    public Mono<ResponseEntity<Boolean>> isCommentLikedByUser(@PathVariable String commentId, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String username = authenticationService.extractUsername(token);

            return postService.isCommentLikedByUser(commentId, username)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.ok(false));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
        }
    }

    @GetMapping("/posts/{postId}/likeCount")
    public Mono<Long> getPostLikeCount(@PathVariable String postId) {
        return postService.getPostLikeCount(postId);
    }

    @GetMapping("/comments/{commentId}/likeCount")
    public Mono<Long> getCommentLikeCount(@PathVariable String commentId) {
        return postService.getCommentLikeCount(commentId);
    }

    @GetMapping("/posts/liked")
    public Flux<PostWithUserInfo> getLikedPosts(@RequestParam int offset, @RequestParam int limit, @RequestHeader("Authorization") String authHeader){
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        String token = authHeader.replace("Bearer ", "");
        String username = authenticationService.extractUsername(token);

        return postService.getLikedPostsByUsername(pageable, username)
                .concatMap(postService::getPostWithUserInfo);
    }


    @PutMapping("/posts/unpublish")
    public Mono<ResponseEntity<Boolean>> unpublishUserPosts(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = authenticationService.extractUsername(token);

        return postService.unpublishUserPosts(username)
                .map(result -> ResponseEntity.ok(result))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PutMapping("/comments/unpublish")
    public Mono<ResponseEntity<Boolean>> unpublishUserComments(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = authenticationService.extractUsername(token);

        return postService.unpublishUserComments(username)
                .map(result -> ResponseEntity.ok(result))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
