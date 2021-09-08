package com.example.meetnature.home.ui.useredit;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.meetnature.MainActivity;
import com.example.meetnature.R;
import com.example.meetnature.controllers.UserController;
import com.example.meetnature.data.models.Event;
import com.example.meetnature.data.models.User;
import com.example.meetnature.helpers.taksiDoBaze;
import com.example.meetnature.home.ui.profile.UserProfileFragment;
import com.example.meetnature.home.ui.viewevent.ViewEventFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class EditUserProfile extends Fragment {

    private EditUserProfileViewModel mViewModel;
    private ImageButton userImage;
    private String imgUrl;
    private StorageReference imageStorage;
    private MainActivity mainActivity;

    private final static int TAKE_PICTURE = 1;

    public static EditUserProfile newInstance() {
        return new EditUserProfile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_user_profile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditUserProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mainActivity = (MainActivity) getActivity();
        imageStorage = FirebaseStorage.getInstance(taksiDoBaze.dbStorage).getReference();
        User user = ((MainActivity)getActivity()).getUser();
        imgUrl = "";

        userImage = (ImageButton) view.findViewById(R.id.edit_user_profile_img);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PICTURE);
                imgUrl = user.getUsername() + " " + new Date().toString() + " " + user.getUid() + ".jpg";
            }
        });

        Button save = view.findViewById(R.id.edit_user_profile_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText infoTxb = (EditText) view.findViewById(R.id.edit_user_info_tbx);
                user.setInfo(infoTxb.getText().toString());

                EditText phoneNum = (EditText) view.findViewById(R.id.edit_user_phone_tbx);
                user.setPhoneNumber(Integer.parseInt(phoneNum.getText().toString()));

                if (!imgUrl.equals("")){
                    user.setImageUrl(imgUrl);

                    ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
                    ((BitmapDrawable)userImage.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 85, imgBytes);
                    imageStorage.child(user.getImageUrl()).putBytes(imgBytes.toByteArray())
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(mainActivity, "Image upload failed", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(mainActivity, "Event picture is added", Toast.LENGTH_SHORT).show();
                            /*
                            EventController.getInstance().addEvent(event, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(mainActivity, "Event is added", Toast.LENGTH_SHORT).show();
                                    //TODO: redirect to event page
                                }
                            });*/
                                }
                            });

                }
                UserController.getInstance().editUser(user, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        mainActivity.getMainFragmentManager().beginTransaction().replace(R.id.main_fragment_container, UserProfileFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PICTURE){
            if (data != null){
                Bitmap pic = data.getParcelableExtra("data");
                userImage.setImageBitmap(pic);
            }
        }
    }
}