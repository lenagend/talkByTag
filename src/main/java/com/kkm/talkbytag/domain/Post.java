package com.kkm.talkbytag.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Post {
    private @Id String id;
    private String hashTag;
    private String authorId;
    private String contents;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime modifiedAt;
    private int liked = 0;
    private int viewCount = 0;
    private boolean published = true;
    private int commentCnt = 0;

    public Post() {
    }

    public Post(String id, String hashTag, String authorId, String contents, LocalDateTime createdAt, LocalDateTime modifiedAt, int liked, int viewCount, boolean published, int commentCnt) {
        this.id = id;
        this.hashTag = hashTag;
        this.authorId = authorId;
        this.contents = contents;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.liked = liked;
        this.viewCount = viewCount;
        this.published = published;
        this.commentCnt = commentCnt;
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

    public int getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(int commentCnt) {
        this.commentCnt = commentCnt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return liked == post.liked && viewCount == post.viewCount && published == post.published && Objects.equals(id, post.id) && Objects.equals(hashTag, post.hashTag) && Objects.equals(authorId, post.authorId) && Objects.equals(contents, post.contents) && Objects.equals(createdAt, post.createdAt) && Objects.equals(modifiedAt, post.modifiedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hashTag, authorId, contents, createdAt, modifiedAt, liked, viewCount, published);
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
                ", modifiedAt=" + modifiedAt +
                ", liked=" + liked +
                ", viewCount=" + viewCount +
                ", published=" + published +
                '}';
    }
}
