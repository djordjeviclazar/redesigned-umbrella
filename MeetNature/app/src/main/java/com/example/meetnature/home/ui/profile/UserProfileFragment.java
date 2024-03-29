package com.example.meetnature.home.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.meetnature.MainActivity;
import com.example.meetnature.R;
import com.example.meetnature.controllers.UserController;
import com.example.meetnature.data.models.Badges;
import com.example.meetnature.data.models.SmallEvent;
import com.example.meetnature.data.models.User;
import com.example.meetnature.home.ui.useredit.EditUserProfile;
import com.example.meetnature.home.ui.viewevent.ViewEventFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;

import com.example.meetnature.helpers.taksiDoBaze;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

    static MainActivity mainActivity;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity)getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.user_profile_fragment, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        View usernameTextView = view.findViewById(R.id.username_lbl);
        View infoTextView = view.findViewById(R.id.info_lbl);
        View userProfileImage = view.findViewById(R.id.user_profile_img);
        ListView badgesListLayout = view.findViewById(R.id.badges_list_layout);
        ListView eventsListLayout = view.findViewById(R.id.events_list_layout);
        Button editUserBtn = view.findViewById(R.id.edit_user_btn);

        UserController.getInstance().refreshCurrentUser(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                editUserBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainActivity.getMainFragmentManager().beginTransaction().replace(R.id.main_fragment_container, EditUserProfile.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                    }
                });

                User user = dataSnapshot.getValue(User.class);
                if (user.getImageUrl().equals("")){
                    Picasso.get().load(taksiDoBaze.defaultImage).resize(200, 200).into((ImageView)userProfileImage);
                }
                else {
                    Picasso.get().load(user.getImageUrl()).resize(200, 200).into((ImageView)userProfileImage);
                }


                ((TextView) usernameTextView).setText(user.getUsername());
                ((TextView) infoTextView).setText(user.getInfo());
                //((ImageView) userProfileImage).(user.getImageUrl());

                // Print events:
                if (user.getOrganizingEvents() != null) {
                    Collection<SmallEvent> smallEvents = user.getOrganizingEvents().values();

                    ArrayList<SmallEvent> param = new ArrayList<>();
                    param.addAll(smallEvents);
                    EventViewAdapter adapter = new EventViewAdapter(getContext(), param);
                    adapter.setOnClickListenerLinkEvent(new OnClickListenerLinkEvent(mainActivity));
                    eventsListLayout.setAdapter(adapter);
                }

                if (user.getBadges() != null) {
                    Collection<Badges> badges = user.getBadges().values();

                    ArrayList<Badges> param = new ArrayList<>();
                    param.addAll(badges);
                    BadgeViewAdapter adapter = new BadgeViewAdapter(getContext(), param);
                    badgesListLayout.setAdapter(adapter);
                }
            }
        });
    }

    public static class OnClickListenerLinkEvent implements View.OnClickListener{

        private String eventUid;
        private MainActivity mainActivity;

        public OnClickListenerLinkEvent(MainActivity mainActivity){
            this.mainActivity = mainActivity;
        }

        public void setEventUid(String eventUid) {
            this.eventUid = eventUid;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("eventUid", eventUid);
            OnClickListenerLinkEvent.this.mainActivity.getMainFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, ViewEventFragment.class, bundle)
                    .setReorderingAllowed(true).addToBackStack(null).commit();
        }
    }
}