package com.example.meetnature;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.example.meetnature.authentication.AuthenticationViewModel;
import com.example.meetnature.controllers.UserController;
import com.example.meetnature.data.models.User;
import com.example.meetnature.home.ui.main.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

public class MainActivity extends AppCompatActivity {

    AuthenticationViewModel authenticationViewModel;
    User user;
    FragmentManager mainFragmentManager;

    private LogedUserCallback logedUserCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainFragmentManager = getSupportFragmentManager();
        authenticationViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        authenticationViewModel.loginUserDTOMutableLiveData.observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser loginUser) {
                if (loginUser != null){
                    UserController.getInstance().setFirebaseUser(loginUser);
                    UserController.getInstance().getUser(new GetUserCallback());
                }
                else {
                    Toast.makeText(MainActivity.this,"NEeeeeeeeeeeeeee", Toast.LENGTH_SHORT).show();
                }
            }
        });
        authenticationViewModel.currentUser();

        setContentView(R.layout.activity_main);
    }

    public AuthenticationViewModel getAuthViewModel(){
        return authenticationViewModel;
    }

    public User getUser() {return user;}

    public FragmentManager getMainFragmentManager() {return mainFragmentManager;}

    public void setLogedUserCallback(LogedUserCallback logedUserCallback){
        this.logedUserCallback = logedUserCallback;
    }

    public class GetUserCallback implements OnSuccessListener<DataSnapshot>{

        @Override
        public void onSuccess(DataSnapshot dataSnapshot) {
             MainActivity.this.user = dataSnapshot.getValue(User.class);

            if(user == null){
                Toast.makeText(MainActivity.this,"NEeeeeeeeeeeeeee user null", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, user.getUsername(), Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Uspesno", Toast.LENGTH_SHORT).show();

                /*
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                homeIntent.putExtra("User", user);
                startActivity(homeIntent);

                 */
                //if(logedUserCallback != null) {
                //logedUserCallback.NavigateToActivity();
                //}

                mainFragmentManager.beginTransaction().replace(R.id.main_fragment_container, HomeFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    public interface LogedUserCallback {
        public void NavigateToActivity();
    }
}