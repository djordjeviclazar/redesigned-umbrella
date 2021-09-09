package com.example.meetnature.home.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meetnature.authentication.AuthenticationController;
import com.example.meetnature.data.models.Event;
import com.example.meetnature.data.models.User;

import java.util.List;

public class HomeViewModel extends ViewModel {

    MutableLiveData<List<Event>> nearEventsLiveData = new MutableLiveData<>();
    MutableLiveData<List<User>> nearUsersLiveData = new MutableLiveData<>();

    public void logout(){
        AuthenticationController controller = AuthenticationController.getInstance();
        controller.logout();
    }

    /*
    public LiveData<List<User>> getNearUsers(){

    }*/
}