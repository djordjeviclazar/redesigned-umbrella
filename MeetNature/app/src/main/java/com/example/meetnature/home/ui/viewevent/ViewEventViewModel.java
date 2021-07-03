package com.example.meetnature.home.ui.viewevent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meetnature.controllers.EventController;
import com.example.meetnature.data.models.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;

public class ViewEventViewModel extends ViewModel {

    MutableLiveData<Event> eventMutableLiveData = new MutableLiveData<>();

    public void getEvent(String uid){
        EventController.getInstance().getEvent(uid, new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);
                eventMutableLiveData.postValue(event);
            }
        });
    }
}