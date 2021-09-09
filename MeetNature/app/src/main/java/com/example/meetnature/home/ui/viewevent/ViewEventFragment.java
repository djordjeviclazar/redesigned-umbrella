package com.example.meetnature.home.ui.viewevent;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetnature.MainActivity;
import com.example.meetnature.R;
import com.example.meetnature.controllers.EventController;
import com.example.meetnature.controllers.UserController;
import com.example.meetnature.data.models.Event;
import com.example.meetnature.data.models.SmallUser;
import com.example.meetnature.data.models.User;
import com.example.meetnature.home.ui.eventinfo.EventInfoFragment;
import com.example.meetnature.home.ui.threewinersspinners.Three_winers_spinners;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewEventFragment extends Fragment {

    private ViewEventViewModel mViewModel;
    private MainActivity mainActivity;
    private FragmentManager mainFragmentManager;
    private MapView mapView;
    private IMapController mapController;
    private String eventUid;
    private Bundle arguments;

    public static ViewEventFragment newInstance() {
        return new ViewEventFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arguments = getArguments();
        eventUid = arguments.getString("eventUid");

        mViewModel = new ViewModelProvider(getActivity()).get(ViewEventViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_event_fragment, container, false);
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

        EventController.getInstance().getEvent(eventUid, new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);
                //eventMutableLiveData.postValue(event);

                //setup map:
                Context context = mainActivity.getApplicationContext();
                Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
                mapView = (MapView)view.findViewById(R.id.view_event_fragment_map);
                mapView.setMultiTouchControls(true);
                mapController = mapView.getController();

                if(mapController != null)
                {
                    mapController.setZoom(15.0);
                    GeoPoint startPoint = new GeoPoint(event.getLat(), event.getLon());
                    mapController.setCenter(startPoint);
                    Marker marker = new Marker(mapView);
                    marker.setPosition(startPoint);
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                    mapView.getOverlays().add(marker);
                }

                String taglist = "";
                /*
                for (String tag : event.getTag()){
                    taglist += tag;
                }*/
                taglist += event.getTag();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
                ((TextView)view.findViewById(R.id.event_tags_lbl)).setText(taglist);
                //((TextView)view.findViewById(R.id.event_place_lbl)).setText(event.getPlace());
                //((TextView)view.findViewById(R.id.event_city_lbl)).setText(event.getCity());
                ((TextView)view.findViewById(R.id.event_date_lbl)).setText(dateFormat.format(event.getTime()));
                ((TextView)view.findViewById(R.id.event_description_lbl)).setText(event.getDescription());
                //((TextView)view.findViewById(R.id.event_country_lbl)).setText(event.getCountry());
                ((TextView)view.findViewById(R.id.event_capacity_lbl)).setText(event.getCapacity() + "");
                ((TextView)view.findViewById(R.id.event_name_lbl)).setText(event.getEventName());
                ((TextView)view.findViewById(R.id.event_owner_lbl)).setText(event.getOrganizer().getUsername());


                view.findViewById(R.id.event_owner_lbl).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: Add link to organizer profile:
                    }
                });

                // calculate tournament value, and check if user is attendant or follower
                boolean isAttendant = false;
                boolean isFollower = false;

                int followersCount = 0, attendantsCount = 0;
                if (event.getAttendants() != null) {
                    attendantsCount = event.getAttendants().size();
                    for (SmallUser attendant : event.getAttendants().values()) {
                        if (attendant.getUid().equals(mainActivity.getUser().getUid())) {
                            isAttendant = true;
                            break;
                        }
                    }
                }
                if (event.getFollowers() != null) {
                    followersCount = event.getFollowers().size();
                    for (SmallUser follower : event.getFollowers().values()) {
                        if (follower.getUid().equals(mainActivity.getUser().getUid())) {
                            isFollower = true;
                            break;
                        }
                    }
                }

                Button action = (Button)view.findViewById(R.id.event_going_btn);
                if (!event.getFinished()) {
                    if (event.getOrganizer().getUid().equals(mainActivity.getUser().getUid())) {
                        if (event.getTime().before(new Date())){
                            if (event.getStarted()){
                                action.setText("Finish event");
                                action.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("eventUid", eventUid);
                                        mainActivity.getMainFragmentManager().beginTransaction().replace(R.id.main_fragment_container, Three_winers_spinners.class, bundle)
                                                .setReorderingAllowed(true).addToBackStack(null).commit();
                                    }
                                });
                            }
                            else {
                                action.setText("Start event");
                                action.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        EventController.getInstance().startEvent(eventUid, new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                Toast.makeText(mainActivity, "You started event!", Toast.LENGTH_SHORT).show();
                                                action.setText("Finish event");
                                                action.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("eventUid", eventUid);
                                                        mainActivity.getMainFragmentManager().beginTransaction().replace(R.id.main_fragment_container, Three_winers_spinners.class, bundle)
                                                                .setReorderingAllowed(true).addToBackStack(null).commit();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }
                        else {
                            action.setEnabled(false);
                            action.setVisibility(View.GONE);
                        }
                    }
                    else {
                        // Set button
                        if (!isFollower && !isAttendant) {
                            if (followersCount >= event.getCapacity()) {
                                action.setEnabled(false);
                                action.setText("NO MORE PLACE!");
                                Toast.makeText(mainActivity, "Sorry, but there is no more place for this event.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (event.getTime().before(new Date()) && event.getStarted()){
                                    action.setText("Attend");
                                    action.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            double currentLat = mainActivity.getUser().getLat();
                                            double currentLon = mainActivity.getUser().getLon();
                                            double distance = GeoFireUtils.getDistanceBetween(new GeoLocation(currentLat, currentLon), new GeoLocation(event.getLat(), event.getLon()));

                                            action.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    EventController.getInstance().followEvent(event, mainActivity.getUser(), new OnSuccessListener() {
                                                        @Override
                                                        public void onSuccess(Object o) {
                                                            action.setEnabled(false);
                                                            Toast.makeText(mainActivity, "You are registered!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                                else {
                                    final int followersCountConst = followersCount;
                                    action.setText("Follow");
                                    action.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (followersCountConst >= event.getCapacity()) {
                                                Toast.makeText(mainActivity, "Sorry, but there is no more place for this event.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                EventController.getInstance().followEvent(event, UserController.getInstance().getCurrentUser(), new OnSuccessListener() {
                                                    @Override
                                                    public void onSuccess(Object o) {
                                                        Toast.makeText(mainActivity, "Your wish to participate in event is forwarded to server. Good luck!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        }
                        else {
                            if (event.getTime().before(new Date()) && event.getStarted()){
                                if (isAttendant){
                                    action.setEnabled(false);
                                    action.setVisibility(View.GONE);
                                }
                                else {
                                    action.setText("Attend!");
                                    action.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            double currentLat = mainActivity.getUser().getLat();
                                            double currentLon = mainActivity.getUser().getLon();
                                            double distance = GeoFireUtils.getDistanceBetween(new GeoLocation(currentLat, currentLon), new GeoLocation(event.getLat(), event.getLon()));
                                            if (distance < 50) {
                                                action.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        EventController.getInstance().attendEvent(event, mainActivity.getUser(), new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {
                                                                action.setEnabled(false);
                                                                Toast.makeText(mainActivity, "Good Luck!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(mainActivity, "You must be on exact place of event to attend. Go to place on map and try again.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                            else{
                                action.setVisibility(View.GONE);
                            }
                        }
                    }
                }
                else {
                    action.setEnabled(true);
                    action.setText("Ranikng List");
                    action.setVisibility(View.VISIBLE);
                    action.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mainActivity.getMainFragmentManager().beginTransaction().replace(R.id.main_fragment_container, EventInfoFragment.class, arguments)
                                    .setReorderingAllowed(true).addToBackStack(null).commit();
                        }
                    });
                }
            }
        });

        /*
        mViewModel.getEvent(eventUid);
        mViewModel.eventMutableLiveData.observe(mainActivity, new Observer<Event>() {
            @Override
            public void onChanged(Event event) {

            }
        });*/
    }
}