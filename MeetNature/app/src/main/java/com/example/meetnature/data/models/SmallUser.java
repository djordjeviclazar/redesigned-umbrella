package com.example.meetnature.data.models;

import java.io.Serializable;

public class SmallUser implements Serializable {

    private String uid;
    private String username;
    private String imageUrl;
    private double lat;
    private double lon;
    private String geoHash;

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

    public double getLat() {
        return  lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public  double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getGeoHash(){
        return geoHash;
    }

    public  void setGeoHash(String geoHash){
        this.geoHash = geoHash;
    }

}
