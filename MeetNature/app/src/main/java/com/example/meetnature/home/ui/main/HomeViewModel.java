package com.example.meetnature.home.ui.main;

import androidx.lifecycle.ViewModel;

import com.example.meetnature.authentication.AuthenticationController;

public class HomeViewModel extends ViewModel {

    public void logout(){
        AuthenticationController controller = AuthenticationController.getInstance();
        controller.logout();
    }
}