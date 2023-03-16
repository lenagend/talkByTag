package com.kkm.talkbytag.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostUnitTest {
    @Test
    void postBasicsShouldWork(){
        Post samplePost = new Post( "hashTag1", "user1",  "contents1");

        assertThat(samplePost.getHashTag()).isEqualTo("hashTag1");
        assertThat(samplePost.getAuthorId()).isEqualTo("user1");
        assertThat(samplePost.getContents()).isEqualTo("contents1");
        assertThat(samplePost.getLiked()).isEqualTo(0);
        assertThat(samplePost.getCreatedAt()).isNotNull();

    }
}
