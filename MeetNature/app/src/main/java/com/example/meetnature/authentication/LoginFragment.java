package com.example.meetnature.authentication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetnature.MainActivity;
import com.example.meetnature.R;
import com.example.meetnature.authentication.data.dtos.LoginUserDTO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private static MainActivity mainActivity;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();

        AuthenticationViewModel authenticationViewModel = ((MainActivity)getActivity()).getAuthViewModel();
        view.findViewById(R.id.RegisterTextLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_LoginFragment_to_RegisterFragment);
            }
        });

        view.findViewById(R.id.firstpage_loginfragment_login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUserDTO loginUser = new LoginUserDTO();
                loginUser.email = ((TextView) view.findViewById(R.id.firstpage_loginfragment_email_txb)).getText().toString();
                loginUser.password = ((TextView) view.findViewById(R.id.firstpage_loginfragment_pass_txb)).getText().toString();

                //((MainActivity) getActivity()).setLogedUserCallback(new LoginFragmentLogedUserCallback());
                MainActivity.loginsClicked = 1;
                authenticationViewModel.loginUser(loginUser);
            }
        });
    }

    public static void ToastHelper(String text)
    {
        Toast.makeText(mainActivity, text, Toast.LENGTH_SHORT).show();
    }

    public class LoginFragmentLogedUserCallback implements MainActivity.LogedUserCallback{

        @Override
        public void NavigateToActivity() {
            /*
            NavHostFragment.findNavController(LoginFragment.this)
                    .navigate(R.id.action_LoginFragment_to_HomeActivity);

             */
        }
    }
}