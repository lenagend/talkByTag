package com.kkm.talkbytag.domain;

import org.junit.jupiter.api.Test;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {
    @Test
    void postBasicsShouldWork(){
        Post samplePost = new Post( "hashTag1", "user1", "title1", "contents1", LocalDateTime.of(2023, 3, 8, 20, 24, 0));

        assertThat(samplePost.getHashTag()).isEqualTo("hashTag1");
        assertThat(samplePost.getWriter()).isEqualTo("user1");
        assertThat(samplePost.getTitle()).isEqualTo("title1");
        assertThat(samplePost.getContents()).isEqualTo("contents1");
        assertThat(samplePost.getCreatedDate()).isEqualTo(LocalDateTime.of(2023, 3, 8, 20, 24, 0));

    }
}
