package com.example.meetnature.home.ui.profile;

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

import com.example.meetnature.data.models.SmallEvent;

import java.util.ArrayList;
import java.util.List;

import com.example.meetnature.R;
import com.example.meetnature.data.models.User;
import com.example.meetnature.helpers.taksiDoBaze;
import com.squareup.picasso.Picasso;

public class EventViewAdapter extends ArrayAdapter<SmallEvent> {

    private Context mainContext;
    private List<SmallEvent> eventList = new ArrayList<>();
    private UserProfileFragment.OnClickListenerLinkEvent onClickListenerLinkEvent;

    public EventViewAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<SmallEvent> list) {
        super(context, 0 , list);
        mainContext = context;
        eventList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        if (item == null){
            item = LayoutInflater.from(mainContext).inflate(R.layout.simple_event_list_item, parent, false);
        }

        SmallEvent smallEvent = eventList.get(position);
        ImageButton img = (ImageButton)item.findViewById(R.id.simple_event_list_item_image);
        if (smallEvent.getImageUrl().equals("")){
            Picasso.get().load(taksiDoBaze.defaultImage).resize(200, 200).into((ImageView)img);
        }
        else {
            Picasso.get().load(smallEvent.getImageUrl()).resize(200, 200).into((ImageView)img);
        }
        //Picasso.get().load(smallEvent.getImageUrl()).resize(100, 100).into(img);

        ((TextView)item.findViewById(R.id.simple_event_list_item_event_name_lbl)).setText(smallEvent.getEventName());
        ((TextView)item.findViewById(R.id.simple_event_list_item_tag_lbl)).setText(smallEvent.getTag());

        onClickListenerLinkEvent.setEventUid(smallEvent.getUId());
        item.findViewById(R.id.simple_event_list_item_link_btn).setOnClickListener(onClickListenerLinkEvent);

        return item;
    }

    public void setOnClickListenerLinkEvent(UserProfileFragment.OnClickListenerLinkEvent onClickListenerLinkEvent) {
        this.onClickListenerLinkEvent = onClickListenerLinkEvent;
    }
}
