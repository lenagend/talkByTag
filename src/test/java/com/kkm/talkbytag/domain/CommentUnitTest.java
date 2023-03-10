package com.kkm.talkbytag.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentUnitTest {
    @Test
    void postBasicsShouldWork(){
        Comment sampleComment = new Comment( "post1", "contents1", "user1");

        assertThat(sampleComment.getPostId()).isEqualTo("post1");
        assertThat(sampleComment.getWriter()).isEqualTo("user1");
        assertThat(sampleComment.getContents()).isEqualTo("contents1");


    }
}
