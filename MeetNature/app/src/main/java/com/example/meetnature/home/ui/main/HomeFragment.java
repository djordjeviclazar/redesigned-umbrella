package com.example.meetnature.home.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.meetnature.R;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // NavController navController = Navigation.findNavController(view);
        //NavController navController = NavHostFragment.findNavController(this);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.home_fragment);
        NavController navController = navHostFragment.getNavController();

        view.findViewById(R.id.homepage_homefragment_floatmap_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NavHostFragment.findNavController(HomeFragment.this)
                navController.navigate(R.id.action_HomeFragment_to_AddEventFragment);
            }
        });

        view.findViewById(R.id.profile_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Uso u profil", Toast.LENGTH_SHORT).show();
                //setContentView(R.layout.user_profile_fragment);
                //NavHostFragment.findNavController(HomeFragment.this)
                //      .navigate(R.id.action_HomeFragment_to_UserProfileFragment);
            }
        });

        view.findViewById(R.id.home_logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.logout();
                Toast.makeText(getActivity(), "LogedOut", Toast.LENGTH_SHORT).show();
                //finish();
                // TODO: Return to Login
                getActivity().recreate();
            }
        });
    }
}