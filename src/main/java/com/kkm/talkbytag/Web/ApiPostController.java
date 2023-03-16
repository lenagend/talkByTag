package com.kkm.talkbytag.Web;

import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.service.PostService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/posts")
    public Flux<Post> getPosts(@RequestParam int offset, @RequestParam int limit){
        return postService.getPosts()
                .filter(Post::isPublished)
                .skip(offset)
                .take(limit);
    }

    @PostMapping("/api/posts")
    public Mono<Post> createPost(@RequestBody Post post){
        return postService.savePost(post);
    }

    @PutMapping("/api/posts/{postId}")
    public Mono<Post> updatePost(@PathVariable String postId, @RequestBody Post post){
        return postService.getPostByPostId(postId)
                .flatMap(p -> {
                    p.setContents(post.getContents());
                    p.setAuthorId(post.getAuthorId());
                    p.setHashTag(post.getHashTag());
                    p.setPublished(post.isPublished());
                    p.setModifiedAt(LocalDateTime.now());
                    return postService.savePost(p);
                });
    }

}
