package com.kkm.talkbytag.dto;

import com.kkm.talkbytag.domain.Post;

import java.time.LocalDateTime;

public class PostWithUserInfo {
    private String id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long likes;
    private int viewCount;
    private boolean published;
    private Long commentCount;
    private String nickname;
    private String profileImage;


    public PostWithUserInfo() {
    }

    public PostWithUserInfo(String id, String title, String username, String contents, LocalDateTime createdAt, LocalDateTime modifiedAt, Long likes, int viewCount, boolean published, Long commentCount, String nickname, String profileImage) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.contents = contents;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.likes = likes;
        this.viewCount = viewCount;
        this.published = published;
        this.commentCount = commentCount;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
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

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
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

    public static PostWithUserInfo fromPost(Post post) {
        PostWithUserInfo postWithUserInfo = new PostWithUserInfo();
        postWithUserInfo.setId(post.getId());
        postWithUserInfo.setTitle(post.getTitle());
        postWithUserInfo.setUsername(post.getUsername());
        postWithUserInfo.setContents(post.getContents());
        postWithUserInfo.setCreatedAt(post.getCreatedAt());
        postWithUserInfo.setModifiedAt(post.getModifiedAt());
        postWithUserInfo.setPublished(post.isPublished());
        postWithUserInfo.setLikes(post.getLikes());
        return postWithUserInfo;
    }
}
