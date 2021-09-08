package com.example.meetnature.home.ui.eventinfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meetnature.MainActivity;
import com.example.meetnature.R;
import com.example.meetnature.controllers.EventController;
import com.example.meetnature.data.models.Event;
import com.example.meetnature.data.models.User;
import com.example.meetnature.helpers.taksiDoBaze;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;

public class EventInfoFragment extends Fragment {

    private MainActivity mainActivity;
    private String eventUid;
    private User currentUser;

    public EventInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        Bundle arguments = getArguments();
        eventUid = arguments.getString("eventUid");

        currentUser = mainActivity.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.event_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventController.getInstance().getEvent(eventUid, new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);

                View userProfileImage = view.findViewById(R.id.event_info_winner_img);
                TextView usernameLabel = (TextView)view.findViewById(R.id.event_info_winner_username_lbl);
                Button linkToProfileBtn = (Button) view.findViewById(R.id.event_info_winner_profile_btn);

                if (event.getWinner().getImageUrl().equals("")){
                    Picasso.get().load(taksiDoBaze.defaultImage).resize(200, 200).into((ImageView)userProfileImage);
                }
                else {
                    Picasso.get().load(event.getWinner().getImageUrl()).resize(200, 200).into((ImageView)userProfileImage);
                }

                usernameLabel.setText(event.getWinner().getUsername());

                if (currentUser.getUid().equals(event.getWinner().getUid())){
                    linkToProfileBtn.setVisibility(View.GONE);
                }
                else {
                    linkToProfileBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: add code for profile access
                        }
                    });
                }
            }
        });
    }
}