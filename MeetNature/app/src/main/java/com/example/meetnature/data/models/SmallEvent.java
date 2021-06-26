package com.example.meetnature.data.models;

import java.util.Date;

public class SmallEvent {

    private String uId;
    private String eventName;
    private String tag;
    private Date time;
    private String imageUrl;


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

}
