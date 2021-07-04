package com.example.meetnature.home.ui.addevent;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetnature.MainActivity;
import com.example.meetnature.R;
import com.example.meetnature.controllers.EventController;
import com.example.meetnature.controllers.UserController;
import com.example.meetnature.data.models.Event;
import com.example.meetnature.home.ui.DatePickerViewModel;
import com.example.meetnature.home.ui.date_picker_fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import com.example.meetnature.helpers.taksiDoBaze;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddEventFragment extends Fragment {

    private AddEventViewModel mViewModel;
    private MainActivity mainActivity;
    private DatePickerViewModel datePickerViewModel;
    private ImageButton eventImage;
    private MapView mapView;
    private IMapController mapController;
    private MyLocationNewOverlay myLocationNewOverlay;
    private StorageReference imageStorage;

    private final static int TAKE_PICTURE = 1;
    private final static int PERMISSION_ACCESS_FINE_LOCATION = 1;

    public static AddEventFragment newInstance() {
        return new AddEventFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(AddEventViewModel.class);
        datePickerViewModel = DatePickerViewModel.getInstance();
        imageStorage = FirebaseStorage.getInstance(taksiDoBaze.dbStorage).getReference();

        return inflater.inflate(R.layout.add_event_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText dateTime = (EditText) view.findViewById(R.id.add_event_datetime_txb);
        eventImage = (ImageButton) view.findViewById(R.id.add_event_image_btn);
        mainActivity = (MainActivity)getActivity();
        datePickerViewModel.mutableLiveData.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                dateTime.setText(s, TextView.BufferType.EDITABLE);
            }
        });

        EditText descTxb = (EditText)view.findViewById(R.id.add_event_description_tbx);
        EditText nameTxb = (EditText)view.findViewById(R.id.add_event_name_tbx);
        EditText tagTxb = (EditText)view.findViewById(R.id.add_event_tags_tbx);

        dateTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if(hasFocus) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, date_picker_fragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PICTURE);
            }
        });

        view.findViewById(R.id.add_event_add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = new Event();
                event.setDescription(descTxb.getText().toString());
                Date eventDate = datePickerViewModel.date == null ? new Date() : datePickerViewModel.date;
                event.setTime(datePickerViewModel.date);
                event.setEventName(nameTxb.getText().toString());
                List<String> tags = new ArrayList<>();
                tags.add(tagTxb.getText().toString());
                event.setTag(tags);
                event.setCapacity(20);
                event.setFinished(false);
                event.setImageUrl(UserController.getInstance().getCurrentUser().getUsername() + "_" + event.getEventName() + new Date().toString() + ".jpg");


                ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
                ((BitmapDrawable)eventImage.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 85, imgBytes);
                imageStorage.child(event.getImageUrl()).putBytes(imgBytes.toByteArray())
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mainActivity, "Image upload failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            EventController.getInstance().addEvent(event, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(mainActivity, "Event is added", Toast.LENGTH_SHORT).show();
                                    //TODO: redirect to event page
                                }
                            });
                        }
                    });

                EventController.getInstance().addEvent(event, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(getActivity(),"Event is created", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        /*
        mapView = (MapView)view.findViewById(R.id.add_event_map);
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
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PICTURE){
            if (data != null){
                Bitmap pic = data.getParcelableExtra("data");
                eventImage.setImageBitmap(pic);
            }
        }
    }

    private void setupMap(){
        setLocation();
    }

    private void setLocation(){
        myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(mainActivity), mapView);
        myLocationNewOverlay.enableMyLocation();
        mViewModel.currentEventPlace = myLocationNewOverlay.getMyLocation();
        mapView.getOverlays().add(myLocationNewOverlay);
        mapController = mapView.getController();
        if (mapController != null){
            mapController.setZoom(15.0);
            mapController.setCenter(mViewModel.currentEventPlace);

            /*
            ArrayList<OverlayItem> items =new ArrayList<>();
            OverlayItem item = new OverlayItem("Location of event", "This will be stored as place of event if you add event", mViewModel.currentEventPlace);
            items.add(item);
            mapView.getOverlays().add(new ItemizedIconOverlay<OverlayItem>(items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                @Override
                public boolean onItemSingleTapUp(int index, OverlayItem item) {
                    return false;
                }

                @Override
                public boolean onItemLongPress(int index, OverlayItem item) {
                    return false;
                }
            }))*/
            Marker marker = new Marker(mapView);
            marker.setPosition(mViewModel.currentEventPlace);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mapView.getOverlays().add(marker);
        }
    }
}