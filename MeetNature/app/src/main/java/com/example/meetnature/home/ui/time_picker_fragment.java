package com.example.meetnature.home.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.example.meetnature.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link time_picker_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class time_picker_fragment extends Fragment {

    DatePickerViewModel mViewModel;


    public time_picker_fragment() {
        // Required empty public constructor
    }


    public static time_picker_fragment newInstance(String param1, String param2) {
        time_picker_fragment fragment = new time_picker_fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = DatePickerViewModel.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.time_picker_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);

        view.findViewById(R.id.time_picker_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.date.setHours(timePicker.getHour());
                mViewModel.date.setMinutes(timePicker.getMinute());
                mViewModel.mutableLiveData.postValue(mViewModel.date.toString());

                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}