package com.example.meetnature.controllers;

import androidx.annotation.NonNull;

import com.example.meetnature.data.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.meetnature.helpers.*;
import com.google.firebase.database.ValueEventListener;

public class UserController {

    //private User user = null;
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
        context.child(firebaseUser.getUid()).get().addOnSuccessListener(callback);
    }

    public void setFirebaseUser(FirebaseUser firebaseUser){

        this.firebaseUser = firebaseUser;

    }

    public void addNewUser(User user, ChildEventListener callback){
        context.addChildEventListener(callback);
        context.child(user.getUid()).setValue(user);

    }

}
