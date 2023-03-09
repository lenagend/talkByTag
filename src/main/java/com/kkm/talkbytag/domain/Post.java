package com.kkm.talkbytag.domain;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class Post {
    private @Id String id;
    private String hashTag;
    private String writer;
    private String contents;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime modifiedDate;
    private int liked = 0;
    private int comments = 0;

    public Post() {
    }

    public Post(String hashTag, String writer,  String contents) {
        this.hashTag = hashTag;
        this.writer = writer;
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

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return liked == post.liked && comments == post.comments && Objects.equals(id, post.id) && Objects.equals(hashTag, post.hashTag) && Objects.equals(writer, post.writer) && Objects.equals(contents, post.contents) && Objects.equals(createdDate, post.createdDate) && Objects.equals(modifiedDate, post.modifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hashTag, writer, contents, createdDate, modifiedDate, liked, comments);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", hashTag='" + hashTag + '\'' +
                ", writer='" + writer + '\'' +
                ", contents='" + contents + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", liked=" + liked +
                ", comments=" + comments +
                '}';
    }
}
