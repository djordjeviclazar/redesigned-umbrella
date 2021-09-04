package com.example.meetnature;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.meetnature.authentication.AuthenticationController;
import com.example.meetnature.authentication.AuthenticationViewModel;
import com.example.meetnature.controllers.EventController;
import com.example.meetnature.controllers.UserController;
import com.example.meetnature.data.models.Event;
import com.example.meetnature.data.models.User;
import com.example.meetnature.home.ui.main.HomeFragment;
import com.example.meetnature.home.ui.profile.UserProfileFragment;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.core.GeoHash;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public static int loginsClicked = -10;

    AuthenticationViewModel authenticationViewModel;
    User user;
    double userLat, userLon;
    String userGeohash;
    FragmentManager mainFragmentManager;
    LocationManager locationManager;

    public static MutableLiveData<List<Event>> nearEvents = new MutableLiveData<>();
    public static MutableLiveData<List<User>> nearUsers = new MutableLiveData<>();
    HashMap<String, Event> inMemoryEvents;

    private LogedUserCallback logedUserCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginsClicked = -10;
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
                    //Toast.makeText(MainActivity.this,"NEeeeeeeeeeeeeee", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {


            this.getMenuInflater().inflate(R.menu.main_menu, menu);
            return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.home_logout_btn)
        {
            //Toast.makeText(this, "LOGOUT IN MENU CLICKED", Toast.LENGTH_SHORT).show();
            AuthenticationController controller = AuthenticationController.getInstance();
            controller.logout();
            mainFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            recreate();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        //Toast.makeText(this, "onLocationChanged Invoked", Toast.LENGTH_SHORT).show();

        userLat = location.getLatitude();
        userLon = location.getLongitude();
        userGeohash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(userLat, userLon));

        if (user != null){
            user.setLon(userLon);
            user.setLat(userLat);
            user.setGeoHash(userGeohash);

            EventController.getInstance().getEventsInRadius(new GeoLocation(userLat, userLon), 500, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {

                    Event eventData = (Event)o;
                    //Toast.makeText(MainActivity.this, eventData.getEventName() + " event is added to MutableLiveData", Toast.LENGTH_SHORT).show();
                    List<Event> previousEvents = nearEvents.getValue() == null ? new ArrayList<>() : nearEvents.getValue();
                    previousEvents.add(eventData);
                    nearEvents.postValue(previousEvents);
                }
            });

            UserController.getInstance().getUsersInRadius(new GeoLocation(userLat, userLon), 500, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    User userData = (User)o;
                    //Toast.makeText(MainActivity.this, eventData.getUsername() + " is added to MutableLiveData", Toast.LENGTH_SHORT).show();
                    List<User> previousUsers = nearUsers.getValue() == null ? new ArrayList<>() : nearUsers.getValue();
                    previousUsers.add(userData);
                    nearUsers.postValue(previousUsers);
                }
            });
        }

        /*
        EventController.getInstance().getNearEventsData(userGeohash, userLat, userLon, new GeoQueryDataEventListener(){
            @Override
            public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                Event eventData = dataSnapshot.getValue(Event.class);
                List<Event> previousEvents = nearEvents.getValue() == null ? new ArrayList<>() : nearEvents.getValue();
                previousEvents.add(eventData);
                nearEvents.postValue(previousEvents);
            }

            @Override
            public void onDataExited(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {

            }

            @Override
            public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
        */

        /*
        EventController.getInstance().getNearEvents(userGeohash, userLat, userLon, new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                EventController.getInstance().getEvent(key, new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Event eventInRange = dataSnapshot.getValue(Event.class);
                        nearEvents.postValue(eventInRange);
                    }
                });
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });*/
    }

    public class GetUserCallback implements OnSuccessListener<DataSnapshot>{

        @Override
        public void onSuccess(DataSnapshot dataSnapshot) {

            nearEvents = new MutableLiveData<>();
            nearUsers = new MutableLiveData<>();

             MainActivity.this.user = dataSnapshot.getValue(User.class);
             user.setLon(userLon);
             user.setLat(userLat);
             user.setGeoHash(userGeohash);

            if(user == null){
                //Toast.makeText(MainActivity.this,"NEeeeeeeeeeeeeee user null", Toast.LENGTH_SHORT).show();
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