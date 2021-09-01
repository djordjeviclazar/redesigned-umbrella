package com.example.meetnature.home.ui.eventlist;

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

import com.example.meetnature.data.models.Event;

import java.util.ArrayList;
import java.util.List;

import com.example.meetnature.R;
import com.example.meetnature.data.models.User;
import com.example.meetnature.home.ui.profile.UserProfileFragment;
import com.squareup.picasso.Picasso;

class BigEventViewAdapter extends ArrayAdapter<Event> {

    private Context mainContext;
    private List<Event> eventList = new ArrayList<>();
    private UserProfileFragment.OnClickListenerLinkEvent onClickListenerLinkEvent;

    public BigEventViewAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Event> list) {
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

        Event Event = eventList.get(position);
        ImageButton img = (ImageButton)item.findViewById(R.id.simple_event_list_item_image);
        Picasso.get().load(Event.getImageUrl()).resize(100, 100).into(img);

        ((TextView)item.findViewById(R.id.simple_event_list_item_event_name_lbl)).setText(Event.getEventName());
        ((TextView)item.findViewById(R.id.simple_event_list_item_tag_lbl)).setText(Event.getTag());
        ((TextView)item.findViewById(R.id.simple_event_list_username)).setText(Event.getOrganizer().getUsername());

        onClickListenerLinkEvent.setEventUid(Event.getUId());
        item.findViewById(R.id.simple_event_list_item_link_btn).setOnClickListener(onClickListenerLinkEvent);

        return item;
    }

    public void setOnClickListenerLinkEvent(UserProfileFragment.OnClickListenerLinkEvent onClickListenerLinkEvent) {
        this.onClickListenerLinkEvent = onClickListenerLinkEvent;
    }
}

