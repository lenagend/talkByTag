package com.kkm.talkbytag.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Comment {
    @Id
    private String id;
    private String postId;
    private String upperCommentId;
    private String authorId;
    private String contents;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime modifiedAt;
    private int liked = 0;
    private boolean published = true;

    public Comment() {
    }

    public Comment(String id, String postId, String upperCommentId, String authorId, String contents, LocalDateTime createdAt, LocalDateTime modifiedAt, int liked, boolean published) {
        this.id = id;
        this.postId = postId;
        this.upperCommentId = upperCommentId;
        this.authorId = authorId;
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

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
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
