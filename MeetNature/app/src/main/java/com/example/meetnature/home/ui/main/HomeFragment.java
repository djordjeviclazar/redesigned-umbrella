package com.example.meetnature.home.ui.main;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.meetnature.MainActivity;
import com.example.meetnature.R;
import com.example.meetnature.data.models.Event;
import com.example.meetnature.helpers.taksiDoBaze;
import com.example.meetnature.home.ui.addevent.AddEventFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private MainActivity mainActivity;
    private FragmentManager mainFragmentManager;
    private static MapView mapView;
    private static IMapController mapController;
    private static MyLocationNewOverlay myLocationNewOverlay;

    private Observer<Event> myObserver;

    final static int PERMISSION_ACCESS_FINE_LOCATION = 1;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        myObserver = null;

        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mainActivity = (MainActivity)getActivity();
        mainFragmentManager = mainActivity.getMainFragmentManager();
        // NavController navController = Navigation.findNavController(view);
        //NavController navController = NavHostFragment.findNavController(this);
        //NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.home_fragment);
        //NavController navController = navHostFragment.getNavController();

        view.findViewById(R.id.homepage_homefragment_floatmap_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_HomeFragment_to_AddEventFragment);*/

                mainFragmentManager.beginTransaction().replace(R.id.main_fragment_container, AddEventFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        view.findViewById(R.id.profile_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Uso u profil", Toast.LENGTH_SHORT).show();
                //setContentView(R.layout.user_profile_fragment);
                //NavHostFragment.findNavController(HomeFragment.this)
                //      .navigate(R.id.action_HomeFragment_to_UserProfileFragment);
            }
        });

        view.findViewById(R.id.home_logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.logout();
                Toast.makeText(getActivity(), "LogedOut", Toast.LENGTH_SHORT).show();
                //finish();
                // TODO: Return to Login
                mainFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getActivity().recreate();
            }
        });

        

        //Toast.makeText(mainActivity, "Loginsclicked = " + MainActivity.loginsClicked, Toast.LENGTH_SHORT).show();
        if (MainActivity.loginsClicked > 0)
        {
            //Toast.makeText(mainActivity, "Calling Logout ON CLICK", Toast.LENGTH_SHORT).show();
            MainActivity.loginsClicked = -10;
            view.findViewById(R.id.home_logout_btn).callOnClick();
        }

        //setup map:
        Context context = mainActivity.getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        mapView = (MapView)view.findViewById(R.id.homepage_homefragment_map);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();

        if(ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(mainActivity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
        }
        else
        {
            setupMap();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (myObserver != null){
            MainActivity.nearEvents.removeObserver(myObserver);
        }
    }

    @SuppressLint("Missing permission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ACCESS_FINE_LOCATION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setupMap();
            }
        }
    }

    private void setupMap(){
        setLocation();
    }

    private void setLocation(){
        myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(mainActivity), mapView);
        myLocationNewOverlay.enableMyLocation();
        mapView.getOverlays().add(myLocationNewOverlay);
        mapController = mapView.getController();
        if (mapController != null){
            mapController.setZoom(15.0);
            myLocationNewOverlay.enableFollowLocation();
            mapController.setCenter(myLocationNewOverlay.getMyLocation());

            setupMarkerCallback(mapView);
        }
    }

    private void setupMarkerCallback(MapView mapViewArg){
        Toast.makeText(getActivity(), "Marker callback", Toast.LENGTH_SHORT).show();
        if (myObserver == null){
            myObserver = new Observer<Event>() {
                @Override
                public void onChanged(Event event) {
                    Toast.makeText(getActivity(), event.getEventName() + " adding to map", Toast.LENGTH_SHORT).show();
                    if (mapViewArg != null) {
                        Marker marker = new Marker(mapViewArg);
                        marker.setPosition(new GeoPoint(event.getLat(), event.getLon()));
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                        // set Image on marker:
                        String imageUrl = event.getImageUrl();
                        Picasso.get().load(imageUrl).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Drawable markerImage = new BitmapDrawable(bitmap);
                                marker.setIcon(markerImage);
                                mapView.getOverlays().add(marker);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                mapView.getOverlays().add(marker);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                    }
                }
            };
        }
        MainActivity.nearEvents.observe(mainActivity, myObserver);
    }
}