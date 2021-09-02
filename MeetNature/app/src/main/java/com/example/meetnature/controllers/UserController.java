package com.example.meetnature.controllers;

import com.example.meetnature.data.models.Badges;
import com.example.meetnature.data.models.Event;
import com.example.meetnature.data.models.SmallEvent;
import com.example.meetnature.data.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.meetnature.helpers.*;

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

    public User getCurrentUser(){
        return user;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser){

        this.firebaseUser = firebaseUser;
        context.child(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }
        });
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

    public void rewardUser(String uid, int value, String tag) {
        Badges badge = new Badges();
        badge.setLevel(1);
        badge.setTag(tag);
        badge.setValue(value);

        String key = context.child(uid).child("badges").push().getKey();
        context.child(uid).child("badges").child(key).setValue(badge);
    }

}
