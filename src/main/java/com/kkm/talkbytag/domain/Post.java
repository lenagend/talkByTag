package com.kkm.talkbytag.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Post {
    private @Id String id;
    private String hashTag;
    private String username;
    private String contents;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime modifiedAt;
    private boolean published = true;


    public Post() {
    }

    public Post(String id, String hashTag, String username, String contents, LocalDateTime createdAt, LocalDateTime modifiedAt, boolean published) {
        this.id = id;
        this.hashTag = hashTag;
        this.username = username;
        this.contents = contents;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.published = published;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
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

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return published == post.published && Objects.equals(id, post.id) && Objects.equals(hashTag, post.hashTag) && Objects.equals(username, post.username) && Objects.equals(contents, post.contents) && Objects.equals(createdAt, post.createdAt) && Objects.equals(modifiedAt, post.modifiedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hashTag, username, contents, createdAt, modifiedAt, published);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", hashTag='" + hashTag + '\'' +
                ", username='" + username + '\'' +
                ", contents='" + contents + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", published=" + published +
                '}';
    }
}
