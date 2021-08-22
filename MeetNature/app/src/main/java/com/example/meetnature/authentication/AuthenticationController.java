package com.example.meetnature.authentication;


import androidx.annotation.NonNull;

import com.example.meetnature.data.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Observable;
import java.util.concurrent.Executor;

public class AuthenticationController implements Executor {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private Observable loginCompleted;

    private static AuthenticationController instance = null;
    private AuthenticationController(){
        firebaseAuth = FirebaseAuth.getInstance();
        loginCompleted = new Observable();
    }

    public static AuthenticationController getInstance(){
        if (instance != null){
            return instance;
        }
        else {
            instance = new AuthenticationController();
            return instance;
        }
    }

    public FirebaseUser getCurrentUser(){
        if (user != null){
            return user;
        }

        user = firebaseAuth.getCurrentUser();
        return user;
    }

    private void setCurrentUser(FirebaseUser user){
        this.user = user;
    }

    /*public void Login(String email, String pass){
        //LoginFragment.ToastHelper();
        try {
            firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(*//*(Executor) this,*//* new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser logedUser = firebaseAuth.getCurrentUser();
                                setCurrentUser(logedUser);
                                loginCompleted.notifyObservers(logedUser);
                            }
                            else {
                                setCurrentUser(null);
                                loginCompleted.notifyObservers();
                            }
                        }
                    });
        }
        catch (Exception e) {
            setCurrentUser(null);
            loginCompleted.notifyObservers();
        }
    }

    public FirebaseUser register(String email, String pass, User newUser){
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser logedUser = firebaseAuth.getCurrentUser();
                                setCurrentUser(logedUser);

                                // TO DO: Add method to store User info (Images, phone number, username, ...)
                                // in UserRepo, or UserController
                            }
                            else {
                                user = null;
                            }
                        }
                    });
            return user;
        }
        catch (Exception e) {
            return null;
        }
    }*/

    public boolean logout(){
        firebaseAuth.signOut();
        user = null;
        return true;
    }

    @Override
    public void execute(Runnable command) {
        command.run();
    }

    public Observable getLoginCompleted(){
        return loginCompleted;
    }

    public FirebaseAuth getFirebaseAuth(){
        return firebaseAuth;
    }
}
