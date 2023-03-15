package com.kkm.talkbytag.Controller;

import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.service.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {

    private PostService postService;

    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    Mono<Rendering> home(Pageable pageable){
        return Mono.just(Rendering.view("home.html")
                .modelAttribute("posts",
                        this.postService.getPosts(pageable)
                                .doOnNext(System.out::println))
                .build());

    }

    @PostMapping("/submit")
    Mono<String> submit(@ModelAttribute Post post){
        post.setWriter("testUser");
        return this.postService.savePost(post).thenReturn("redirect:/");
    }

    @DeleteMapping("/delete/{id}")
    Mono<String> delete(@PathVariable String id){
        return this.postService.deletePostById(id).thenReturn("redirect:/");
    }
}
