package com.kkm.talkbytag.Web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkm.talkbytag.domain.Comment;
import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.dto.CommentWithUserInfo;
import com.kkm.talkbytag.dto.PostWithUserInfo;
import com.kkm.talkbytag.service.PostService;
import com.kkm.talkbytag.service.UserInfoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiPostController {

    private final PostService postService;
    private final UserInfoService userInfoService;

    public ApiPostController(PostService postService, UserInfoService userInfoService) {
        this.postService = postService;
        this.userInfoService = userInfoService;
    }

    @GetMapping("/posts")
    public Flux<PostWithUserInfo> getPosts(@RequestParam int offset, @RequestParam int limit){
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postService.getPosts(pageable, true)
                .concatMap(postService::getPostWithUserInfo);
    }

    @GetMapping("/posts/read/{id}")
    public Mono<PostWithUserInfo> getPostById(@PathVariable String id){
        return postService.getPostByPostId(id)
                .flatMap(postService::getPostWithUserInfo);
    }

    @GetMapping("/posts/search")
    public Flux<PostWithUserInfo> searchByHashTag(@RequestParam("q") String q, @RequestParam int offset, @RequestParam int limit){
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        boolean startsWithAtSign = q != null && !q.isEmpty() && q.charAt(0) == '#';
        if(startsWithAtSign){
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

}
