package com.kkm.talkbytag.controller;

import com.kkm.talkbytag.Web.ApiPostController;
import com.kkm.talkbytag.domain.Comment;
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
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        post2.setPublished(false);
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
                    assertThat(responseBody.get(1)).isEqualTo(post1);
                });

        verify(postService, times(1)).getPosts();
    }

    @Test
    void testCreatePost() {
        Post post = new Post("hashTag1", "id1", "content1");
        given(postService.savePost(post)).willReturn(Mono.just(post));

        webClient.post()
                .uri("/api/posts")
                .body(Mono.just(post), Post.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Post.class)
                .isEqualTo(post);

        verify(postService, times(1)).savePost(post);
    }

    @Test
    void testUpdatePost() {
        Post post = new Post("1","hashTag1", "id1", "content1");
        given(postService.getPostByPostId("1")).willReturn(Mono.just(post));
        given(postService.savePost(post)).willReturn(Mono.just(post));

        Post updatedPost = new Post("1", "hashTag1", "hashTag1",  "Author2");
        webClient.put()
                .uri("/api/posts/1")
                .body(Mono.just(updatedPost), Post.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Post.class)
                .consumeWith(response -> {
                    Post responseBody = response.getResponseBody();
                    assertThat(responseBody.getModifiedAt()).isNotNull();
                    System.out.println(responseBody.getModifiedAt());
                });

        verify(postService, times(1)).getPostByPostId("1");
        verify(postService, times(1)).savePost(post);
    }



}
