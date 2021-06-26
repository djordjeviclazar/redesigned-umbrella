package com.example.meetnature.data.models;

import java.util.Date;
import java.util.List;

public class Event {

    private String uId;
    private String eventName;
    private String description;
    private String imageUrl;
    private Date time;
    private String tag;
    private int capacity;
    private List<SmallUser> attendants;
    private double lat;
    private double lon;
    private String geoHash;

    public String getId() {
        return uId;
    }

    public void setId(String uId) {
        this.uId = uId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<SmallUser> getAttendants() {
        return attendants;
    }

    public void setAttendants(List<SmallUser> attendants) {
        this.attendants = attendants;
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
