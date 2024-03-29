package com.example.meetnature.controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.meetnature.MainActivity;
import com.example.meetnature.data.models.Badges;
import com.example.meetnature.data.models.Event;
import com.example.meetnature.data.models.SmallEvent;
import com.example.meetnature.data.models.User;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.meetnature.helpers.*;
import com.google.firebase.database.ServerValue;
import com.google.type.DateTime;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class UserController {

    private User user = null;
    private FirebaseUser firebaseUser = null;
    private static UserController instance = null;
    private DatabaseReference context;

    private UserController(){
        context = FirebaseDatabase.getInstance(taksiDoBaze.dbURL).getReference().child("User");
    }

    public static UserController getInstance(){
        if (instance != null){
            return instance;
        }
        else {
            instance = new UserController();
            return instance;
        }
    }

    public void getUser(OnSuccessListener<DataSnapshot> callback){
        Task<DataSnapshot> task = context.child(firebaseUser.getUid()).get();
        task.addOnSuccessListener(callback);
    }

    public void getUser(String uid, OnSuccessListener<DataSnapshot> callback){
        Task<DataSnapshot> task = context.child(uid).get();
        task.addOnSuccessListener(callback);
    }

    public void refreshCurrentUser(OnSuccessListener<DataSnapshot> callback){
        if (user != null){
            context.child(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    callback.onSuccess(dataSnapshot);
                }
            });
        }
    }

    public User getCurrentUser(){
        return user;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser){

        this.firebaseUser = firebaseUser;
        context.child(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                setActive();
            }
        });
    }

    public void editUser(User newUser, OnSuccessListener callback) {
        context.child(newUser.getUid()).child("imageUrl").setValue(newUser.getImageUrl());
        context.child(newUser.getUid()).child("info").setValue(newUser.getInfo());
        context.child(newUser.getUid()).child("phoneNumber").setValue(newUser.getPhoneNumber());

        callback.onSuccess(newUser);
    }

    public void addNewUser(User user, OnSuccessListener callback){
        context.child(user.getUid()).setValue(user).addOnSuccessListener(callback);
    }

    public void addEvent(Event event, OnSuccessListener callback){
        SmallEvent smallEvent = new SmallEvent();
        smallEvent.setUId(event.getUId());
        smallEvent.setEventName(event.getEventName());
        smallEvent.setGeoHash(event.getGeoHash());
        smallEvent.setImageUrl(event.getImageUrl());
        smallEvent.setLat(event.getLat());
        smallEvent.setLon(event.getLon());
        smallEvent.setTime(event.getTime());

        context.child(user.getUid()).child("organizingEvents").child(event.getUId()).setValue(smallEvent).addOnSuccessListener(callback);
    }

    public void followEvent(Event event, OnSuccessListener callback){
        SmallEvent smallEvent = new SmallEvent();
        smallEvent.setUId(event.getUId());
        smallEvent.setEventName(event.getEventName());
        smallEvent.setGeoHash(event.getGeoHash());
        smallEvent.setImageUrl(event.getImageUrl());
        smallEvent.setLat(event.getLat());
        smallEvent.setLon(event.getLon());
        smallEvent.setTime(event.getTime());

        context.child(user.getUid()).child("followingEvents").child(event.getUId()).setValue(smallEvent).addOnSuccessListener(callback);
    }

    public void rewardUser(String uid, int value, String tag, String eventUid, String eventName, OnSuccessListener finishEvent) {
        Badges badge = new Badges();
        badge.setLevel(1);
        badge.setTag(tag);
        badge.setValue(value);
        badge.setEventUid(eventUid);
        badge.setEventName(eventName);

        context.child(uid).child("score").setValue(ServerValue.increment(value));

        String key = context.child(uid).child("badges").push().getKey();
        context.child(uid).child("badges").child(key).setValue(badge).addOnSuccessListener(finishEvent);
    }

    public void getUsersInRadius(GeoLocation center, double radius, MainActivity.UserInRangeCallback callback){
        /*
        context.get().addOnSuccessListener(dataSnapshot -> {
            for (DataSnapshot data : dataSnapshot.getChildren()){
                User userData = data.getValue(User.class);

                if (isInRangeActive(userData, center, radius)) {
                    callback.onSuccess(userData);
                }
            }
        });
         */
        context.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User userData = snapshot.getValue(User.class);
                if (isInRangeActive(userData, center, radius)) {
                    callback.newUser(userData);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User userData = snapshot.getValue(User.class);
                if (isInRangeActive(userData, center, radius)) {
                    callback.updateUser(userData);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isInRangeActive(User otherUser, GeoLocation center, double radius){

        boolean isInRange = GeoFireUtils.getDistanceBetween(center, new GeoLocation(otherUser.getLat(), otherUser.getLon())) <= radius;
        boolean isActive = otherUser.isActive();
        boolean notCurrentUser = !otherUser.getUid().equals(user.getUid());

        // check if isActive is set before 15 mins:
        Date lastActive = otherUser.getLastActive();

        Date now = new Date();
        Calendar calendarNow  = Calendar.getInstance();
        calendarNow.setTime(now);
        calendarNow.add(Calendar.MINUTE, -15);
        boolean isBefore  = lastActive == null ? false : calendarNow.getTime().before(lastActive);

        return isInRange && notCurrentUser && isActive && isBefore;
    }

    // Only works if currentUser is not null, don't call outside of Activity
    public void updateCurrentUserLocation(double lat, double lon) {
        Date now = new Date();

        if (user != null){
            /*
            user.setLat(lat);
            user.setLon(lon);
            user.setGeoHash(GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lon)));
             */
            Map<String, Object> userLocationProperties = new HashMap<>();
            userLocationProperties.put("lat", lat);
            userLocationProperties.put("lon", lon);
            userLocationProperties.put("geohash", GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lon)));
            userLocationProperties.put("isActive", true);
            userLocationProperties.put("lastActive", now);

            context.child(user.getUid()).updateChildren(userLocationProperties);
        }
    }

    public void orderUsersByScore(OnSuccessListener callback){
        context.orderByChild("score").get().addOnSuccessListener(dataSnapshot -> {
            Stack<User> users = new Stack<>();
            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                User userData = userSnapshot.getValue(User.class);
                users.push(userData);
            }
            callback.onSuccess(users);
        });
    }

    public void setActive(){
        Date now = new Date();

        Map<String, Object> userLocationProperties = new HashMap<>();
        userLocationProperties.put("isActive", true);
        userLocationProperties.put("lastActive", now);

        context.child(user.getUid()).updateChildren(userLocationProperties);
    }

    public void setInactive(){
        Map<String, Object> userLocationProperties = new HashMap<>();
        userLocationProperties.put("isActive", false);

        context.child(user.getUid()).updateChildren(userLocationProperties);
    }
}
