package com.example.meetnature.data.models;

import java.io.Serializable;
import java.util.Date;

public class SmallEvent implements Serializable {

    private String uId;
    private String eventName;
    private String tag;
    private Date time;
    private String imageUrl;
    private double lat;
    private double lon;
    private String geoHash;

    public SmallEvent() {

        this.eventName = "";
        this.tag = "";
        this.time = null;
        this.imageUrl = "";

    }


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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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
