package com.example.meetnature;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.meetnature.authentication.AuthenticationViewModel;
import com.example.meetnature.controllers.UserController;
import com.example.meetnature.data.models.User;
import com.example.meetnature.home.ui.main.HomeFragment;
import com.example.meetnature.home.ui.profile.UserProfileFragment;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.core.GeoHash;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

public class MainActivity extends AppCompatActivity implements LocationListener {

    AuthenticationViewModel authenticationViewModel;
    User user;
    double userLat, userLon;
    String userGeohash;
    FragmentManager mainFragmentManager;
    LocationManager locationManager;


    private LogedUserCallback logedUserCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainFragmentManager = getSupportFragmentManager();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, this);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, this);
        }


        authenticationViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        authenticationViewModel.loginUserDTOMutableLiveData.observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser loginUser) {
                if (loginUser != null){
                    UserController.getInstance().setFirebaseUser(loginUser);
                    UserController.getInstance().getUser(new GetUserCallback());
                }
                else {
                    Toast.makeText(MainActivity.this,"NEeeeeeeeeeeeeee", Toast.LENGTH_SHORT).show();
                }
            }
        });
        authenticationViewModel.currentUser();

        setContentView(R.layout.activity_main);
    }

    public AuthenticationViewModel getAuthViewModel(){
        return authenticationViewModel;
    }

    public User getUser() {return user;}

    public FragmentManager getMainFragmentManager() {return mainFragmentManager;}

    public void setLogedUserCallback(LogedUserCallback logedUserCallback){
        this.logedUserCallback = logedUserCallback;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
            userLat = location.getLatitude();
            userLon = location.getLongitude();
            userGeohash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(userLat, userLon));

            if (user != null){
                user.setLon(userLon);
                user.setLat(userLat);
                user.setGeoHash(userGeohash);
            }
    }

    public class GetUserCallback implements OnSuccessListener<DataSnapshot>{

        @Override
        public void onSuccess(DataSnapshot dataSnapshot) {
             MainActivity.this.user = dataSnapshot.getValue(User.class);
             user.setLon(userLon);
             user.setLat(userLat);
             user.setGeoHash(userGeohash);

            if(user == null){
                Toast.makeText(MainActivity.this,"NEeeeeeeeeeeeeee user null", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, user.getUsername(), Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Uspesno", Toast.LENGTH_SHORT).show();

                findViewById(R.id.main_activity_footer).setVisibility(View.VISIBLE);
                findViewById(R.id.main_activity_events).setVisibility(View.VISIBLE);
                findViewById(R.id.main_activity_user_image_btn).setVisibility(View.VISIBLE);
                findViewById(R.id.main_activity_username).setVisibility(View.VISIBLE);

                findViewById(R.id.main_activity_user_image_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainFragmentManager.beginTransaction().replace(R.id.main_fragment_container, UserProfileFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                    }
                });

                findViewById(R.id.main_activity_username).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainFragmentManager.beginTransaction().replace(R.id.main_fragment_container, UserProfileFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                    }
                });

                /*
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                homeIntent.putExtra("User", user);
                startActivity(homeIntent);

                 */
                //if(logedUserCallback != null) {
                //logedUserCallback.NavigateToActivity();
                //}

                mainFragmentManager.beginTransaction().replace(R.id.main_fragment_container, HomeFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    public interface LogedUserCallback {
        public void NavigateToActivity();
    }
}