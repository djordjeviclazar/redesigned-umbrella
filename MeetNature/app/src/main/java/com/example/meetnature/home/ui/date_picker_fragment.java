package com.example.meetnature.home.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.meetnature.R;
import com.example.meetnature.home.ui.addevent.AddEventViewModel;
import com.example.meetnature.home.ui.main.HomeFragment;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link date_picker_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class date_picker_fragment extends Fragment {

    DatePickerViewModel mViewModel;
    public date_picker_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment date_picker_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static date_picker_fragment newInstance(String param1, String param2) {
        date_picker_fragment fragment = new date_picker_fragment();
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
        return inflater.inflate(R.layout.date_picker_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatePicker datePicker = ((DatePicker)view.findViewById(R.id.date_picker));
        view.findViewById(R.id.date_picker_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.date = new Date();
                mViewModel.date.setYear(datePicker.getYear() - 1900);
                mViewModel.date.setMonth(datePicker.getMonth() - 1);
                mViewModel.date.setDate(datePicker.getDayOfMonth());

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, time_picker_fragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}