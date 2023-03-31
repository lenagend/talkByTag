package com.kkm.talkbytag.domain;

public class UserInfo {
    private Long id;
    private String nickname;
    private String profileImage;
    private Long postCount;
    private Long commentCount;
    private Long likeCount;

    public UserInfo() {
    }

    public UserInfo(Long id, String nickname, String profileImage, Long postCount, Long commentCount, Long likeCount) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.postCount = postCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}