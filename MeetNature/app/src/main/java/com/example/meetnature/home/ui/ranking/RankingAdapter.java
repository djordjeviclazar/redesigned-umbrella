package com.example.meetnature.home.ui.ranking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.meetnature.MainActivity;
import com.example.meetnature.R;
import com.example.meetnature.controllers.EventController;
import com.example.meetnature.controllers.UserController;
import com.example.meetnature.data.models.SmallUser;
import com.example.meetnature.data.models.User;
import com.example.meetnature.helpers.taksiDoBaze;
import com.example.meetnature.home.ui.otherprofile.OtherProfileFragment;
import com.example.meetnature.home.ui.threewinersspinners.WinnerSpinnerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RankingAdapter extends ArrayAdapter<User> {

    private Context mainContext;
    private List<User> users;
    private MainActivity mainActivity;

    public RankingAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<User> list, MainActivity mainActivity) {
        super(context, 0 , list);
        mainContext = context;
        users = list;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        if (item == null){
            item = LayoutInflater.from(mainContext).inflate(R.layout.ranking_user_item, parent, false);
        }

        User userData = users.get(position);
        ImageButton img = (ImageButton)item.findViewById(R.id.ranking_user_img);
        // Must add start path to Storage, and check if image is empty string:
        if (userData.getImageUrl().equals("")){
            Picasso.get().load(taksiDoBaze.defaultImage).resize(200, 200).into((ImageView)img);
        }
        else {
            Picasso.get().load(userData.getImageUrl()).resize(200, 200).into((ImageView)img);
        }
        //Picasso.get().load(smallUser.getImageUrl()).resize(100, 100).into(img);

        ((TextView)item.findViewById(R.id.ranking_user_username_lbl)).setText(userData.getUsername());
        ((TextView)item.findViewById(R.id.ranking_user_rating_lbl)).setText("Rating: " + userData.getScore());

        item.findViewById(R.id.ranking_user_link_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userUid", userData.getUid());
                mainActivity.getMainFragmentManager().beginTransaction().replace(R.id.main_fragment_container, OtherProfileFragment.class, bundle)
                        .setReorderingAllowed(true).addToBackStack(null).commit();
            }
        });


        return item;
    }
}
