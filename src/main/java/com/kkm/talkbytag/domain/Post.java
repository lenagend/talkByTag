package com.kkm.talkbytag.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    private @Id String id;
    private String hashTag;
    private String authorId;
    private String contents;
    private LocalDateTime createdAt = LocalDateTime.now();
    private int liked = 0;
    private int viewCount = 0;

    public Post() {
    }

    public Post(String id, String hashTag, String authorId, String contents) {
        this.id = id;
        this.hashTag = hashTag;
        this.authorId = authorId;
        this.contents = contents;
    }

    public Post(String hashTag, String authorId, String contents) {
        this.hashTag = hashTag;
        this.authorId = authorId;
        this.contents = contents;
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


    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return liked == post.liked && viewCount == post.viewCount && Objects.equals(id, post.id) && Objects.equals(hashTag, post.hashTag) && Objects.equals(authorId, post.authorId) && Objects.equals(contents, post.contents) && Objects.equals(createdAt, post.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hashTag, authorId, contents, createdAt, liked, viewCount);
    }

    @Override
    public String
    toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", hashTag='" + hashTag + '\'' +
                ", authorId='" + authorId + '\'' +
                ", contents='" + contents + '\'' +
                ", createdAt=" + createdAt +
                ", liked=" + liked +
                ", viewCount=" + viewCount +
                '}';
    }
}
