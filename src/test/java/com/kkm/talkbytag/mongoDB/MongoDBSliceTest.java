package com.kkm.talkbytag.mongoDB;

import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class MongoDBSliceTest {
    @Autowired
    PostRepository postRepository;

    @Test
    void postRepositorySavesPosts(){
        Post samplePost = new Post();
        samplePost.setTitle("title1");

        postRepository.save(samplePost)
                .as(StepVerifier::create)
                .expectNextMatches(post -> {
                    assertThat(post.getId()).isNotNull();
                    assertThat(post.getTitle()).isEqualTo("title1");
                    System.out.println(post.toString());
                    return true;
                }).verifyComplete();
    }
}
