package com.example.meetnature.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meetnature.MainActivity;
import com.example.meetnature.authentication.data.dtos.LoginUserDTO;
import com.example.meetnature.controllers.UserController;
import com.example.meetnature.data.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Observable;
import java.util.Observer;

public class AuthenticationViewModel extends ViewModel {

    public MutableLiveData<FirebaseUser> loginUserDTOMutableLiveData = new MutableLiveData<>();
    private Observer loginObserver;
    private UserController userController;

    //private int numberCalled = 0, numberCalledOnComplete = 0;

    public AuthenticationViewModel(){
        userController = UserController.getInstance();
    }

    public void currentUser(){
        AuthenticationController authenticationController = AuthenticationController.getInstance();

        FirebaseUser user = authenticationController.getCurrentUser();
        loginUserDTOMutableLiveData.postValue(user);
    }

    public void loginUser(LoginUserDTO loginUserDTO){
        AuthenticationController authenticationController = AuthenticationController.getInstance();

        //LoginFragment.ToastHelper((++numberCalled) + " ViewModel loginUser call");
        /*
        this.loginObserver = new LoginObserver();
        authenticationController.getLoginCompleted().addObserver(this.loginObserver);
         authenticationController.Login(loginUserDTO.email, loginUserDTO.password);

         */
        FirebaseAuth firebaseAuth = authenticationController.getFirebaseAuth();
        try {
            firebaseAuth.signInWithEmailAndPassword(loginUserDTO.email, loginUserDTO.password)
                    .addOnCompleteListener(/*(Executor) this,*/ new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //LoginFragment.ToastHelper((++numberCalledOnComplete) + " onCompleteSignIn call");
                            if (task.isSuccessful()){
                                FirebaseUser logedUser = firebaseAuth.getCurrentUser();
                                loginUserDTOMutableLiveData.postValue(logedUser);
                                MainActivity.loginsClicked++;
                            }
                            else {
                                loginUserDTOMutableLiveData.postValue(null);
                            }
                        }
                    });
        }
        catch (Exception e) {
            loginUserDTOMutableLiveData.postValue(null);
        }
    }

    public void registerUser(LoginUserDTO loginUserDTO){
        AuthenticationController authenticationController = AuthenticationController.getInstance();

        FirebaseAuth firebaseAuth = authenticationController.getFirebaseAuth();
        try {
            firebaseAuth.createUserWithEmailAndPassword(loginUserDTO.email, loginUserDTO.password)
                    .addOnCompleteListener(/*(Executor) this,*/ new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser logedUser = firebaseAuth.getCurrentUser();
                                User user = new User();
                                user.setEmail(loginUserDTO.email);
                                user.setUsername(loginUserDTO.username);
                                user.setUid(logedUser.getUid());

                                userController.addNewUser(user, new RegisteredUserSuccesListener(logedUser));

                                MainActivity.loginsClicked++;
                            }
                            else {
                                loginUserDTOMutableLiveData.postValue(null);
                            }
                        }
                    });
        }
        catch (Exception e) {
            loginUserDTOMutableLiveData.postValue(null);
        }
    }

    public class LoginObserver implements Observer{

        @Override
        public void update(Observable o, Object arg) {
            /*
            FirebaseUser user = (FirebaseUser) arg;
            loginUserDTOMutableLiveData.postValue(user);
            */

        }
    }

    public class RegisteredUserSuccesListener implements OnSuccessListener{

        private FirebaseUser logedUser;

        public RegisteredUserSuccesListener(FirebaseUser logedUser){
            this.logedUser = logedUser;
        }

        @Override
        public void onSuccess(Object o) {
            loginUserDTOMutableLiveData.postValue(logedUser);
        }
    }
}
