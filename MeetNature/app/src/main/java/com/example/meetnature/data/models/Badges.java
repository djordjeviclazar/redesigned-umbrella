package com.example.meetnature.data.models;

import java.io.Serializable;

public class Badges implements Serializable {

    private String tag;
    private int level;
    private int value;

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

}
