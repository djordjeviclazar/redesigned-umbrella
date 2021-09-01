package com.example.meetnature.home.ui.threewinersspinners;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meetnature.R;

public class Three_winers_spinners extends Fragment {

    private ThreeWinersSpinnersViewModel mViewModel;

    public static Three_winers_spinners newInstance() {
        return new Three_winers_spinners();
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

}