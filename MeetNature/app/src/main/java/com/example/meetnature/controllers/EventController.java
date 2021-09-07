package com.example.meetnature.controllers;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.meetnature.MainActivity;
import com.example.meetnature.data.models.Event;
import com.example.meetnature.data.models.SmallUser;
import com.example.meetnature.data.models.User;
import com.example.meetnature.helpers.taksiDoBaze;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryBounds;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventController {

    private UserController userController;
    private User user;
    private DatabaseReference context;
    private GeoFire geoFireContext;
    //private GeoFire geoFireReference;
    private GeoQuery geoQuery;

    private static EventController instance = null;

    private EventController(){
        userController = UserController.getInstance();
        user = UserController.getInstance().getCurrentUser();
        context = FirebaseDatabase.getInstance(taksiDoBaze.dbURL).getReference().child("Event");
        geoFireContext = new GeoFire(context);
        //geoFireReference = new GeoFire(FirebaseDatabase.getInstance(taksiDoBaze.dbURL).getReference().child("EventLocations"));
        geoQuery = null;
    }

    public static EventController getInstance(){
        if (instance == null){
            instance = new EventController();
        }

        return instance;
    }

    public void addEvent(Event event, OnSuccessListener callback){
        String uid = context.push().getKey();
        event.setUId(uid);
        SmallUser organizer = new SmallUser();
        User currentUser = userController.getCurrentUser();
        organizer.setImageUrl(currentUser.getImageUrl());
        organizer.setUid(currentUser.getUid());
        organizer.setUsername(currentUser.getUsername());
        event.setOrganizer(organizer);

        Map<String, Object> geoFireObject = new HashMap<>();
        geoFireObject.put("geohash", GeoFireUtils.getGeoHashForLocation(new GeoLocation(event.getLat(), event.getLon())));
        geoFireObject.put("lat", event.getLat());
        geoFireObject.put("lon", event.getLon());

        //geoFireReference.setLocation(uid, geoFireObject);

        userController.addEvent(event, new SmallEventAddedCallback(uid, event, callback));

    }

    public void getEvent(String uid, OnSuccessListener<DataSnapshot> callback){
        context.child(uid).get().addOnSuccessListener(callback);
    }

    public void followEvent(Event event, User user, OnSuccessListener callback){
        SmallUser smallUser = new SmallUser();
        smallUser.setUid(user.getUid());
        smallUser.setUsername(user.getUsername());
        smallUser.setImageUrl(user.getImageUrl());
        context.child(event.getUId()).child(user.getUid()).setValue(smallUser);
        UserController.getInstance().followEvent(event, callback);
    }

    public void attendEvent(Event event, User user, OnSuccessListener callback){
        SmallUser smallUser = new SmallUser();
        smallUser.setUid(user.getUid());
        smallUser.setUsername(user.getUsername());
        smallUser.setImageUrl(user.getImageUrl());
        context.child(event.getUId()).child("attendants").setValue(smallUser);
        UserController.getInstance().followEvent(event, callback);
    }

    public void getSearchedEvents(String tag, OnSuccessListener callback){

        context.get().addOnSuccessListener(dataSnapshot -> {
            ArrayList<Event> result = new ArrayList<>();
            for (DataSnapshot data : dataSnapshot.getChildren()){
                Event eventData = data.getValue(Event.class);
                if (eventData.getTag().equals(tag)) {
                    result.add(eventData);
                }
            }
            callback.onSuccess(result);
        });
    }

    public void getEventsInRadius(GeoLocation center, double radius, OnSuccessListener callback){
        /*
        context.get().addOnSuccessListener(dataSnapshot -> {
            for (DataSnapshot data : dataSnapshot.getChildren()){
                Event eventData = data.getValue(Event.class);
                if (GeoFireUtils.getDistanceBetween(center, new GeoLocation(eventData.getLat(), eventData.getLon())) <= radius) {
                    callback.onSuccess(eventData);
                }
            }
        });
         */
        context.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Event eventData = snapshot.getValue(Event.class);
                if (GeoFireUtils.getDistanceBetween(center, new GeoLocation(eventData.getLat(), eventData.getLon())) <= radius) {
                    callback.onSuccess(eventData);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

    public void getNearEvents(String currentGeoHash, double currentLat, double currentLon, GeoQueryEventListener callback){
        if (geoQuery == null) {
            //geoQuery = geoFireReference.queryAtLocation(new GeoLocation(currentLat, currentLon), 50.);
            geoQuery = geoFireContext.queryAtLocation(new GeoLocation(currentLat, currentLon), 0.5);
            geoQuery.addGeoQueryEventListener(callback);
        }
        else{
            geoQuery.setCenter(new GeoLocation(currentLat, currentLon));
        }

//        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(new GeoLocation(currentLat, currentLon), 50);
//        List<Task<DataSnapshot>> queries = new ArrayList<>();
//        for(GeoQueryBounds bound : bounds){
//            Query q = context.orderByChild("geoHash").startAt(bound.startHash).endAt(bound.endHash);/*.addChildEventListener(callback);*/
//            queries.add(q.get());
//            //queries.add(query.get());
//            //query.get().addOnSuccessListener(callback);
//        }
//
//        Tasks.whenAllComplete(queries).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
//            @Override
//            public void onComplete(@NonNull Task<List<Task<?>>> task) {
//                task.getResult();
//            }
//        });
/*
        Tasks.whenAllSuccess(queries).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {
                List<DataSnapshot> results = new ArrayList<>();
                for(Object a : objects){
                    (DataSnapshot)a.get
                }
            }
        })*/
    }

    public void getNearEventsData(String currentGeoHash, double currentLat, double currentLon, GeoQueryDataEventListener callback){
        if(geoQuery == null){
            geoQuery = geoFireContext.queryAtLocation(new GeoLocation(currentLat, currentLon), 500.);
            geoQuery.addGeoQueryDataEventListener(callback);
        }
        else {
            geoQuery.setCenter(new GeoLocation(currentLat, currentLon));
        }
    }

    public void finishEvent(String uid, OnSuccessListener onSuccessListener){
        context.child(uid).child("finished").setValue(true).addOnSuccessListener(onSuccessListener);
    }

    public void startEvent(String uid, OnSuccessListener onSuccessListener) {
        context.child(uid).child("started").setValue(true).addOnSuccessListener(onSuccessListener);
    }

    // callbacks:

    public class SmallEventAddedCallback implements OnSuccessListener{

        private String uid;
        private Event event;
        private OnSuccessListener callback;

        public SmallEventAddedCallback(String uid, Event event, OnSuccessListener callback){
            this.uid = uid;
            this.event = event;
            this.callback = callback;
        }

        @Override
        public void onSuccess(Object o) {

            context.child(uid).setValue(event).addOnSuccessListener(callback);
        }
    }
}
