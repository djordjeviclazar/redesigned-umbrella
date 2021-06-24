package com.example.meetnature.authentication;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class AuthenticationController implements Executor {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private static AuthenticationController instance = null;
    private AuthenticationController(){
        firebaseAuth = FirebaseAuth.getInstance();
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

    public FirebaseUser Login(String email, String pass){
        try {
            firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser logedUser = firebaseAuth.getCurrentUser();
                                setCurrentUser(logedUser);
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
    }

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
