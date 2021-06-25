package com.example.meetnature.data.models;

import java.util.List;

public class User {

    private String uid;
    private String username;
    private String email;
    private String info;
    private String imageUrl;
    private int score;
    private List<Badges> badges;
    private List<SmallEvent> followingEvents;
    private List<SmallUser> friends;
    private List<SmallUser> following;
    private List<SmallUser> followers;
    private double lat;
    private double lon;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Badges> getBadges() {
        return badges;
    }

    public void setBadges(List<Badges> badges) {
        this.badges = badges;
    }

    public List<SmallEvent> getFollowingEvents() {
        return followingEvents;
    }

    public void setFollowingEvents(List<SmallEvent> followingEvents) {
        this.followingEvents = followingEvents;
    }

    public List<SmallUser> getFriends() {
        return friends;
    }

    public void setFriends(List<SmallUser> friends) {
        this.friends = friends;
    }

    public List<SmallUser> getFollowing() {
        return following;
    }

    public void setFollowing(List<SmallUser> following) {
        this.following = following;
    }

    public List<SmallUser> getFollowers() {
        return followers;
    }

    public void setFollowers(List<SmallUser> followers) {
        this.followers = followers;
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

}