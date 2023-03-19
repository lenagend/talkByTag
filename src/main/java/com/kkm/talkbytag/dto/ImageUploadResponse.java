package com.kkm.talkbytag.dto;

public class ImageUploadResponse {
    private String location;

    public ImageUploadResponse(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}