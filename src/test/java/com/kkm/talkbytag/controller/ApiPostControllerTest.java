package com.kkm.talkbytag.controller;

import com.kkm.talkbytag.Controller.ApiPostController;
import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(SpringExtension.class)
@WebFluxTest(ApiPostController.class)
public class ApiPostControllerTest {

    @Autowired
    private WebTestClient webClient;


    @MockBean
    private PostService postService;

    @Test
    void testGetPosts() {
        Post post1 = new Post("hashTag1", "id1", "content1");
        Post post2 = new Post("hashTag2", "id2", "content2");
        Post post3 = new Post("hashTag3", "id3", "content3");
        List<Post> posts = Arrays.asList(post3, post2, post1);
        int offset = 0;
        int limit = 2;
        given(postService.getPosts())
                .willReturn(Flux.fromIterable(posts).log());

        webClient.get()
                .uri("/api/posts?offset=" + offset + "&limit=" + limit)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Post.class)
                .hasSize(limit)
                .consumeWith(response -> {
                    List<Post> responseBody = response.getResponseBody();
                    assertThat(responseBody.get(0)).isEqualTo(post3);
                    assertThat(responseBody.get(1)).isEqualTo(post2);
                });

        verify(postService, times(1)).getPosts();
    }


}
