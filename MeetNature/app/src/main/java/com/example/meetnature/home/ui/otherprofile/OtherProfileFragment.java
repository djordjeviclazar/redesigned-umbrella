package com.example.meetnature.home.ui.otherprofile;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.meetnature.MainActivity;
import com.example.meetnature.R;
import com.example.meetnature.controllers.UserController;
import com.example.meetnature.data.models.SmallEvent;
import com.example.meetnature.data.models.User;
import com.example.meetnature.helpers.taksiDoBaze;
import com.example.meetnature.home.ui.profile.EventViewAdapter;
import com.example.meetnature.home.ui.viewevent.ViewEventFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

public class OtherProfileFragment extends Fragment {

    private OtherProfileViewModel mViewModel;
    private MainActivity mainActivity;
    private FragmentManager mainFragmentManager;
    private String userUid;

    public static OtherProfileFragment newInstance() {
        return new OtherProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        userUid = arguments.getString("userUid");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.other_profile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OtherProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity)getActivity();
        mainFragmentManager = mainActivity.getMainFragmentManager();

        ListView eventsListLayout = view.findViewById(R.id.other_events_list_layout);

        UserController.getInstance().getUser(userUid, new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);



                View userProfileImage = view.findViewById(R.id.other_profile_img);
                ((TextView)view.findViewById(R.id.other_username_lbl)).setText(user.getUsername());
                ((TextView)view.findViewById(R.id.other_info_lbl)).setText(user.getInfo());



                if (user.getImageUrl().equals("")){
                    Picasso.get().load(taksiDoBaze.defaultImage).resize(200, 200).into((ImageView)userProfileImage);
                }
                else {
                    Picasso.get().load(user.getImageUrl()).resize(200, 200).into((ImageView)userProfileImage);
                }

                if (user.getOrganizingEvents() != null) {
                    Collection<SmallEvent> smallEvents = user.getOrganizingEvents().values();

                    ArrayList<SmallEvent> param = new ArrayList<>();
                    param.addAll(smallEvents);
                    EventViewAdapter adapter = new EventViewAdapter(getContext(), param);
                    adapter.setOnClickListenerLinkEvent(new OnClickListenerLinkEvent(mainActivity));
                    eventsListLayout.setAdapter(adapter);
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