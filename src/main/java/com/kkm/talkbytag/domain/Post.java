package com.kkm.talkbytag.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Document
public class Post {
    private @Id String id;
    String hashTag;
    String writer;
    String title;
    String contents;
    LocalDateTime createdDate;
    LocalDateTime modifiedDate;

    public Post() {
    }

    public Post(String hashTag, String writer, String title, String contents, LocalDateTime createdDate) {
        this.hashTag = hashTag;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdDate = createdDate;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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


    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", hashTag='" + hashTag + '\'' +
                ", writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(hashTag, post.hashTag) && Objects.equals(writer, post.writer) && Objects.equals(title, post.title) && Objects.equals(contents, post.contents) && Objects.equals(createdDate, post.createdDate) && Objects.equals(modifiedDate, post.modifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hashTag, writer, title, contents, createdDate, modifiedDate);
    }
}
