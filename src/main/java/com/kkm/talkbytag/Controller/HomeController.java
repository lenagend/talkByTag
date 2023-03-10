package com.kkm.talkbytag.Controller;

import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.service.PostService;
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
    Mono<Rendering> home(){
        return Mono.just(Rendering.view("noCssHome.html")
                .modelAttribute("posts",
                        this.postService.getPosts().doOnNext(System.out::println))
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
