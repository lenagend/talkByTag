package com.kkm.talkbytag.Controller;

import com.kkm.talkbytag.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
        return Mono.just(Rendering.view("home.html")
                .modelAttribute("posts",
                        this.postService.getPosts().doOnNext(System.out::println))
                .build());

    }
}
