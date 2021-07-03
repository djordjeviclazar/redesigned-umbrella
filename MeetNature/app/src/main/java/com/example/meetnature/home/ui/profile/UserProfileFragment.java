package com.example.meetnature.home.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meetnature.MainActivity;
import com.example.meetnature.R;
import com.example.meetnature.data.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.user_profile_fragment, container, false);

        /*View usernameTextView = root.findViewById(R.id.username_lbl);
        View infoTextView = root.findViewById(R.id.info_lbl);
        View userProfileImage = root.findViewWithTag(R.id.user_profile_img);
        View badgesListLayout = root.findViewById(R.id.badges_list_layout);
        View eventsListLayout = root.findViewById(R.id.events_list_layout);

        User user = ((MainActivity)getActivity()).getUser();



        ((TextView) usernameTextView).setText(user.getUsername());
        ((TextView) infoTextView).setText(user.getInfo());
        //((ImageView) userProfileImage).(user.getImageUrl());
        */
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        View usernameTextView = view.findViewById(R.id.username_lbl);
        View infoTextView = view.findViewById(R.id.info_lbl);
        View userProfileImage = view.findViewWithTag(R.id.user_profile_img);
        View badgesListLayout = view.findViewById(R.id.badges_list_layout);
        View eventsListLayout = view.findViewById(R.id.events_list_layout);

        User user = ((MainActivity)getActivity()).getUser();



        ((TextView) usernameTextView).setText(user.getUsername());
        ((TextView) infoTextView).setText(user.getInfo());
        //((ImageView) userProfileImage).(user.getImageUrl());


    }
}