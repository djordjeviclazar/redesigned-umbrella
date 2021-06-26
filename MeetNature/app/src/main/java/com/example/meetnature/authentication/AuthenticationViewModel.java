package com.example.meetnature.authentication;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meetnature.authentication.data.dtos.LoginUserDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Observable;
import java.util.Observer;

public class AuthenticationViewModel extends ViewModel {

    public MutableLiveData<FirebaseUser> loginUserDTOMutableLiveData = new MutableLiveData<>();
    private Observer loginObserver;

    public void currentUser(){
        AuthenticationController authenticationController = AuthenticationController.getInstance();

        FirebaseUser user = authenticationController.getCurrentUser();
        loginUserDTOMutableLiveData.postValue(user);
    }

    public void loginUser(LoginUserDTO loginUserDTO){
        AuthenticationController authenticationController = AuthenticationController.getInstance();

        /*
        this.loginObserver = new LoginObserver();
        authenticationController.getLoginCompleted().addObserver(this.loginObserver);
         authenticationController.Login(loginUserDTO.email, loginUserDTO.password);

         */
        FirebaseAuth firebaseAuth = authenticationController.getFirebaseAuth();
        try {
            firebaseAuth.signInWithEmailAndPassword(loginUserDTO.email, loginUserDTO.password)
                    .addOnCompleteListener(/*(Executor) this,*/ new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser logedUser = firebaseAuth.getCurrentUser();
                                loginUserDTOMutableLiveData.postValue(logedUser);
                            }
                            else {
                                loginUserDTOMutableLiveData.postValue(null);
                            }
                        }
                    });
        }
        catch (Exception e) {
            loginUserDTOMutableLiveData.postValue(null);
        }
    }

    public void registerUser(LoginUserDTO loginUserDTO){
        AuthenticationController authenticationController = AuthenticationController.getInstance();

        FirebaseAuth firebaseAuth = authenticationController.getFirebaseAuth();
        try {
            firebaseAuth.createUserWithEmailAndPassword(loginUserDTO.email, loginUserDTO.password)
                    .addOnCompleteListener(/*(Executor) this,*/ new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser logedUser = firebaseAuth.getCurrentUser();
                                loginUserDTOMutableLiveData.postValue(logedUser);
                            }
                            else {
                                loginUserDTOMutableLiveData.postValue(null);
                            }
                        }
                    });
        }
        catch (Exception e) {
            loginUserDTOMutableLiveData.postValue(null);
        }
    }

    public class LoginObserver implements Observer{

        @Override
        public void update(Observable o, Object arg) {
            FirebaseUser user = (FirebaseUser) arg;
            loginUserDTOMutableLiveData.postValue(user);
        }
    }
}
