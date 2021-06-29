package com.example.meetnature.authentication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(AuthenticationViewModel authenticationViewModel) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*Bundle args = getArguments();
            authenticationViewModel = (AuthenticationViewModel) args.getSerializable("viewModel");*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.loginTextLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(RegisterFragment.this)
                        .navigate(R.id.action_RegisterFragment_to_LoginFragment);
            }
        });

        view.findViewById(R.id.firstpage_registerfragment_register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((TextView)view.findViewById(R.id.firstpage_registerfragment_email_txb)).getText().toString();
                String username = ((TextView)view.findViewById(R.id.firstpage_registerfragment_username_txb)).getText().toString();
                String password = ((TextView)view.findViewById(R.id.firstpage_registerfragment_password_txb)).getText().toString();
                String repeatPassword = ((TextView)view.findViewById(R.id.firstpage_registerfragment_passwordrepeat_txb)).getText().toString();

                if (!password.equals(repeatPassword)){
                    ((TextView)view.findViewById(R.id.firstpage_registerfragment_password_txb)).setText("");
                    ((TextView)view.findViewById(R.id.firstpage_registerfragment_passwordrepeat_txb)).setText("");
                    Toast.makeText(getActivity(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthenticationViewModel authenticationViewModel = ((MainActivity)getActivity()).getAuthViewModel();
                LoginUserDTO loginUser = new LoginUserDTO();
                loginUser.email = email;
                loginUser.password = password;
                loginUser.username = username;
                authenticationViewModel.registerUser(loginUser);
            }
        });
    }
}