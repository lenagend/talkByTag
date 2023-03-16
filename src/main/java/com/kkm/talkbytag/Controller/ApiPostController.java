package com.kkm.talkbytag.Controller;

import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/posts")
    public Flux<Post> getPosts(@RequestParam int offset, @RequestParam int limit){
        return postService.getPosts()
                .skip(offset)
                .take(limit);
    }
}
