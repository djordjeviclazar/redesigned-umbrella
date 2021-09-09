package com.example.meetnature.data.models;

import java.io.Serializable;

public class Badges implements Serializable {

    private String tag;
    private int level;
    private int value;
    private String eventUid;
    private String eventName;

    public Badges() {

        this.tag = "";
        this.level = 0;
        this.value = 0;

    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public String getEventUid() {
        return eventUid;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
