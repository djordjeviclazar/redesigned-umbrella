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

    public WinnerSpinnerAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<SmallUser> list) {
        super(context, 0 , list);
        mainContext = context;
        attendantsList = list;
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
        Picasso.get().load(smallUser.getImageUrl()).resize(100, 100).into(img);

        ((TextView)item.findViewById(R.id.spinner_user_list_item_username_lbl)).setText(smallUser.getUsername());

        item.findViewById(R.id.big_event_list_item_link_btn).setOnClickListener(onClickListener);

        return item;
    }

    public void setOnClickListener(View.OnClickListener onClickListenerLinkEvent) {
        this.onClickListener = onClickListenerLinkEvent;
    }
}
