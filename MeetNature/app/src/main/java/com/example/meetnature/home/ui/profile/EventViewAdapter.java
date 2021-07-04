package com.example.meetnature.home.ui.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.meetnature.data.models.SmallEvent;

import java.util.ArrayList;
import java.util.List;

import com.example.meetnature.R;
import com.squareup.picasso.Picasso;

public class EventViewAdapter extends ArrayAdapter<SmallEvent> {

    private Context mainContext;
    private List<SmallEvent> eventList = new ArrayList<>();

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
        ImageButton img= (ImageButton)item.findViewById(R.id.simple_event_list_item_image);
        Picasso.get().load(smallEvent.getImageUrl()).resize(100, 100).into(img);

        return item;
    }
}
