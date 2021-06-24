package com.example.meetnature.data.models;

import java.util.Date;
import java.util.List;

public class Event {

    private int id;
    private String eventName;
    private String description;
    private String imageUrl;
    private Date time;
    private String tag;
    private int capacity;
    private List<SmallUser> attendants;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}
