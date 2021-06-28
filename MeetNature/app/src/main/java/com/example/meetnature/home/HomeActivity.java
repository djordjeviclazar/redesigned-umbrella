package com.example.meetnature.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.meetnature.R;
import com.example.meetnature.authentication.RegisterFragment;
import com.example.meetnature.data.models.User;
import com.example.meetnature.home.ui.main.HomeFragment;
import com.example.meetnature.home.ui.main.HomeViewModel;

public class HomeActivity extends AppCompatActivity {

    HomeViewModel homeViewModel;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, HomeFragment.newInstance())
                    .commitNow();
        }

        user = (User) getIntent().getSerializableExtra("User");

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        findViewById(R.id.profile_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Uso u profil", Toast.LENGTH_SHORT).show();
                //setContentView(R.layout.user_profile_fragment);
                //NavHostFragment.findNavController(HomeFragment.this)
                //      .navigate(R.id.action_HomeFragment_to_UserProfileFragment);
            }
        });

        findViewById(R.id.home_logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.logout();
                Toast.makeText(HomeActivity.this, "LogedOut", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public User getUser() {return user;}

}