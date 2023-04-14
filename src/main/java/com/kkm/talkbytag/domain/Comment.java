package com.kkm.talkbytag.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

public class Comment {
    @Id
    private String id;
    private String postId;
    private String upperCommentId;
    private String username;
    private String contents;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime modifiedAt;
    private int liked = 0;
    private boolean published = true;

    public Comment() {
    }

    public Comment(String id, String postId, String upperCommentId, String username, String contents, LocalDateTime createdAt, LocalDateTime modifiedAt, int liked, boolean published) {
        this.id = id;
        this.postId = postId;
        this.upperCommentId = upperCommentId;
        this.username = username;
        this.contents = contents;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.liked = liked;
        this.published = published;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUpperCommentId() {
        return upperCommentId;
    }

    public void setUpperCommentId(String upperCommentId) {
        this.upperCommentId = upperCommentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }


}
