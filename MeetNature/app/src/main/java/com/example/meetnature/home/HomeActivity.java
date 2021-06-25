package com.example.meetnature.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.meetnature.R;
import com.example.meetnature.home.ui.main.HomeFragment;
import com.example.meetnature.home.ui.main.HomeViewModel;

public class HomeActivity extends AppCompatActivity {

    HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, HomeFragment.newInstance())
                    .commitNow();
        }

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        findViewById(R.id.home_logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.logout();
                Toast.makeText(HomeActivity.this, "LogedOut", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}