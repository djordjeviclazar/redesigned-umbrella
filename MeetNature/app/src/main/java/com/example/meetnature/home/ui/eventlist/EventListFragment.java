package com.example.meetnature.home.ui.eventlist;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.meetnature.MainActivity;
import com.example.meetnature.R;
import com.example.meetnature.controllers.EventController;
import com.example.meetnature.data.models.Event;
import com.example.meetnature.home.ui.profile.UserProfileFragment;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class EventListFragment extends Fragment {

    private EventListViewModel mViewModel;
    private String tag;
    private MainActivity mainActivity;

    public static EventListFragment newInstance() {
        return new EventListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        tag = arguments.getString("tag");

        mainActivity = (MainActivity)getActivity();
        //Log.v("tag:", tag);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         EventController.getInstance().getSearchedEvents(tag, new OnSuccessListener() {
             @Override
             public void onSuccess(Object o) {
                 ArrayList<Event> params = (ArrayList<Event>)o;
                 BigEventViewAdapter adapter = new BigEventViewAdapter(getContext(), params);
                 adapter.setOnClickListenerLinkEvent(new UserProfileFragment.OnClickListenerLinkEvent(mainActivity));
                 ((ListView) (view.findViewById(R.id.event_list_main_scroll_2))).setAdapter(adapter);
             }
         });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventListViewModel.class);
        // TODO: Use the ViewModel
    }

}