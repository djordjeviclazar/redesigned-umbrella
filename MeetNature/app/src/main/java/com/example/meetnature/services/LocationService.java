package com.example.meetnature.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.meetnature.controllers.EventController;
import com.example.meetnature.controllers.UserController;

public class LocationService extends Service implements LocationListener {

    UserController userController;
    LocationManager locationManager;

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


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            //ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, this);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, this);
        }

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
        refreshOnFiveMinutes.start();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        userController.updateCurrentUserLocation(location.getLatitude(), location.getLongitude());
    }
}
