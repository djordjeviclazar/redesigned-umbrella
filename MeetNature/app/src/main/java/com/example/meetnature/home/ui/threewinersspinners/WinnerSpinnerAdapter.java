package com.example.meetnature.home.ui.threewinersspinners;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.example.meetnature.R;
import com.example.meetnature.controllers.EventController;
import com.example.meetnature.controllers.UserController;
import com.example.meetnature.data.models.SmallEvent;
import com.example.meetnature.data.models.SmallUser;
import com.example.meetnature.helpers.taksiDoBaze;
import com.example.meetnature.home.ui.profile.UserProfileFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WinnerSpinnerAdapter extends ArrayAdapter<SmallUser> {


    private Context mainContext;
    private List<SmallUser> attendantsList = new ArrayList<>();
    private OnSuccessListener onSuccessListener;
    private int value;
    private String tag;
    private String eventUid;
    private String eventName;

    public WinnerSpinnerAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<SmallUser> list, int value, String tag, String eventUid, String eventName) {
        super(context, 0 , list);
        mainContext = context;
        attendantsList = list;
        this.value = value;
        this.tag = tag;
        this.eventUid = eventUid;
        this.eventName = eventName;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        if (item == null){
            item = LayoutInflater.from(mainContext).inflate(R.layout.ranking_user_item, parent, false);
        }

        SmallUser smallUser = attendantsList.get(position);
        ImageButton img = (ImageButton)item.findViewById(R.id.spinner_user_list_item_image);
        // Must add start path to Storage, and check if image is empty string:
        if (smallUser.getImageUrl().equals("")){
            Picasso.get().load(taksiDoBaze.defaultImage).resize(200, 200).into((ImageView)img);
        }
        else {
            Picasso.get().load(smallUser.getImageUrl()).resize(200, 200).into((ImageView)img);
        }
        //Picasso.get().load(smallUser.getImageUrl()).resize(100, 100).into(img);

        ((TextView)item.findViewById(R.id.spinner_user_list_item_username_lbl)).setText(smallUser.getUsername());

        item.findViewById(R.id.spinner_user_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserController.getInstance()
                        .rewardUser(smallUser.getUid(),
                                WinnerSpinnerAdapter.this.value,
                                WinnerSpinnerAdapter.this.tag,
                                eventUid,
                                eventName,
                                new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        EventController.getInstance().setWinner(eventUid, smallUser, onSuccessListener);
                                    }
                                });
            }
        });


        return item;
    }

    public void setOnSuccessListener(OnSuccessListener listener) {
        this.onSuccessListener = listener;
    }
}
