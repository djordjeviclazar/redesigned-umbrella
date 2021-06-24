package com.example.meetnature.authentication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meetnature.authentication.data.dtos.LoginUserDTO;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationViewModel extends ViewModel {

    public MutableLiveData<FirebaseUser> loginUserDTOMutableLiveData = new MutableLiveData<>();

    public void currentUser(){
        AuthenticationController authenticationController = AuthenticationController.getInstance();

        FirebaseUser user = authenticationController.getCurrentUser();
        loginUserDTOMutableLiveData.postValue(user);
    }

    public void loginUser(LoginUserDTO loginUserDTO){
        AuthenticationController authenticationController = AuthenticationController.getInstance();

        FirebaseUser user = authenticationController.Login(loginUserDTO.email, loginUserDTO.password);
        loginUserDTOMutableLiveData.postValue(user);
    }
}
