package com.kkm.talkbytag.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Liked {
    @Id
    private String id;
    private String username;
    private String postId;
    private String commentId;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Liked() {
    }

    public Liked(String id, String username, String postId, String commentId, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.postId = postId;
        this.commentId = commentId;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
