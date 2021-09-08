package com.example.meetnature.home.ui.threewinersspinners;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.meetnature.R;
import com.example.meetnature.controllers.UserController;
import com.example.meetnature.data.models.SmallEvent;
import com.example.meetnature.data.models.SmallUser;
import com.example.meetnature.home.ui.profile.UserProfileFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WinnerSpinnerAdapter extends ArrayAdapter<SmallUser> {


    private Context mainContext;
    private List<SmallUser> attendantsList = new ArrayList<>();
    private View.OnClickListener onClickListener;
    private int value;
    private String tag;

    public WinnerSpinnerAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<SmallUser> list, int value, String tag) {
        super(context, 0 , list);
        mainContext = context;
        attendantsList = list;
        this.value = value;
        this.tag = tag;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        if (item == null){
            item = LayoutInflater.from(mainContext).inflate(R.layout.spinner_user_item, parent, false);
        }

        SmallUser smallUser = attendantsList.get(position);
        ImageButton img = (ImageButton)item.findViewById(R.id.spinner_user_list_item_image);
        // Must add start path to Storage, and check if image is empty string:
        Picasso.get().load(smallUser.getImageUrl()).resize(100, 100).into(img);

        ((TextView)item.findViewById(R.id.spinner_user_list_item_username_lbl)).setText(smallUser.getUsername());

        item.findViewById(R.id.big_event_list_item_link_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserController.getInstance().rewardUser(smallUser.getUid(), WinnerSpinnerAdapter.this.value, WinnerSpinnerAdapter.this.tag);
            }
        });


        return item;
    }

    public void setOnClickListener(View.OnClickListener onClickListenerLinkEvent) {
        this.onClickListener = onClickListenerLinkEvent;
    }
}
