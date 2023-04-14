package com.kkm.talkbytag.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class UserInfo {
    private String username;
    private String nickname;
    private String profileImage;
    private Long postCount;
    private Long commentCount;
    private Long likeCount;
    private LocalDateTime modifiedAt;


    public UserInfo() {
    }

    public UserInfo(String username, String nickname, String profileImage, Long postCount, Long commentCount, Long likeCount, LocalDateTime modifiedAt) {
        this.username = username;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.postCount = postCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.modifiedAt = modifiedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


    public Long getPostCount() {
        return postCount;
    }

    public void setPostCount(Long postCount) {
        this.postCount = postCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}