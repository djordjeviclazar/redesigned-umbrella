package com.example.meetnature.data.models;

import java.util.List;

public class ArchivedUser {

    private String uid;
    private List<Event> organizedEvents;
    private List<Event> visitedEvents;
    private List<Event> followedEvents;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Event> getOrganizedEvents() {
        return organizedEvents;
    }

    public void setOrganizedEvents(List<Event> organizedEvents) {
        this.organizedEvents = organizedEvents;
    }

    public List<Event> getVisitedEvents() {
        return visitedEvents;
    }

    public void setVisitedEvents(List<Event> visitedEvents) {
        this.visitedEvents = visitedEvents;
    }

    public List<Event> getFollowedEvents() {
        return followedEvents;
    }

    public void setFollowedEvents(List<Event> followedEvents) {
        this.followedEvents = followedEvents;
    }

}
