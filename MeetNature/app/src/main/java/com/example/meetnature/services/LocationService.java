package com.example.meetnature.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.meetnature.controllers.EventController;
import com.example.meetnature.controllers.UserController;

public class LocationService extends Service implements LocationListener {

    UserController userController;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        userController = UserController.getInstance();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        Thread refreshOnFiveMinutes = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    userController.setActive();

                    Thread.sleep(10000);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.e("Puko servis", "NEEEEEEEEEEEEEEEEEEEEEEEE: ");
                }

            }
        });
        refreshOnFiveMinutes.run();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        userController.updateCurrentUserLocation(location.getLatitude(), location.getLongitude());
    }
}
