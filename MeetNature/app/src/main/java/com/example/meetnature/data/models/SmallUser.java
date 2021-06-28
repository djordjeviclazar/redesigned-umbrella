package com.example.meetnature.data.models;

import java.io.Serializable;

public class SmallUser implements Serializable {

    private String uid;
    private String username;
    private String imageUrl;

    public SmallUser() {

        this.username = "";
        this.imageUrl = "";

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
