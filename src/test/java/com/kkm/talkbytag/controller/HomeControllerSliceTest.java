package com.kkm.talkbytag.controller;

import com.kkm.talkbytag.Controller.HomeController;
import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(HomeController.class)
public class HomeControllerSliceTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    PostService postService;

    @Test
    void homepage(){
        when(postService.getPosts()).thenReturn(Flux.just(
                new Post( "hashTag1", "user1",  "contents1")
        ));

        client.get().uri("/").exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(exchangeResult ->{
                   assertThat(exchangeResult.getResponseBody().contains("title1"));
                });
    }
}
