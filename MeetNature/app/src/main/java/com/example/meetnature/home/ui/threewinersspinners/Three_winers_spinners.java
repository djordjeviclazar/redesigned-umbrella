package com.example.meetnature.home.ui.threewinersspinners;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.meetnature.MainActivity;
import com.example.meetnature.R;
import com.example.meetnature.controllers.EventController;
import com.example.meetnature.controllers.UserController;
import com.example.meetnature.data.models.Event;
import com.example.meetnature.data.models.SmallUser;
import com.example.meetnature.data.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class Three_winers_spinners extends Fragment {

    private ThreeWinersSpinnersViewModel mViewModel;
    private String eventUid;
    private ArrayList<SmallUser> attendats;

    public static Three_winers_spinners newInstance() {
        return new Three_winers_spinners();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        eventUid = arguments.getString("eventUid");
        attendats = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.three_winers_spinners_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ThreeWinersSpinnersViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView eventsListLayout = view.findViewById(R.id.attendants_list_layout);
        EventController.getInstance().getEvent(eventUid, new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                // populate
                Event event = dataSnapshot.getValue(Event.class);
                attendats.addAll(event.getAttendants().values());

                WinnerSpinnerAdapter goldAdapter = new WinnerSpinnerAdapter(getContext(), attendats, 10, event.getTag());
                goldAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventController.getInstance().finishEvent(eventUid);
                        //UserController.getInstance().rewardUser();
                    }
                });
                eventsListLayout.setAdapter(goldAdapter);
            }
        });
    }
}