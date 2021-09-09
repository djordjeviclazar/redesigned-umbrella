package com.example.meetnature.data.models;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class User implements Serializable {

    private String uid;
    private String username;
    private String email;
    private String info;
    private String imageUrl;
    private int phoneNumber;
    private int score;
    private Map<String, Badges> badges;
    private Map<String, SmallEvent> organizingEvents;
    private Map<String, SmallEvent> followingEvents;
    private Map<String, SmallUser> friends;
    private Map<String, SmallUser> following;
    private Map<String, SmallUser> followers;
    private double lat;
    private double lon;
    private String geoHash;
    private boolean isActive;
    private Date lastActive;

    public User(){

        this.username = "";
        this.email = "";
        this.info = "";
        this.imageUrl = "";
        this.score = 0;
        this.phoneNumber = 0;
        this.lat = 0;
        this.lon = 0;

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

    public int getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber( int phoneNumber ) { this.phoneNumber = phoneNumber; }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Map<String, Badges> getBadges() {
        return badges;
    }

    public void setBadges(Map<String, Badges> badges) {
        this.badges = badges;
    }

    public Map<String, SmallEvent> getOrganizingEvents() {
        return organizingEvents;
    }

    public void setOrganizingEvents(Map<String, SmallEvent> followingEvents) {
        this.organizingEvents = followingEvents;
    }

    public Map<String, SmallEvent> getFollowingEvents() {
        return followingEvents;
    }

    public void setFollowingEvents(Map<String, SmallEvent> followingEvents) {
        this.followingEvents = followingEvents;
    }

    public Map<String, SmallUser> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, SmallUser> friends) {
        this.friends = friends;
    }

    public Map<String, SmallUser> getFollowing() {
        return following;
    }

    public void setFollowing(Map<String, SmallUser> following) {
        this.following = following;
    }

    public Map<String, SmallUser> getFollowers() {
        return followers;
    }

    public void setFollowers(Map<String, SmallUser> followers) {
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

    public String getGeoHash(){
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public boolean getIsActive() {return isActive;}

    public boolean isActive() {
        return isActive;
    }

    public Date getLastActive() {
        return lastActive;
    }

    public void setLastActive(Date lastActive) {
        this.lastActive = lastActive;
    }
}