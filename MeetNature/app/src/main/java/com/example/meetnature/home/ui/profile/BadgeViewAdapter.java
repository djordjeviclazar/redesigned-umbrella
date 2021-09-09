package com.example.meetnature.home.ui.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.meetnature.R;
import com.example.meetnature.data.models.Badges;

import java.util.ArrayList;
import java.util.List;

public class BadgeViewAdapter extends ArrayAdapter<Badges> {

    private Context mainContext;
    private List<Badges> badgeList = new ArrayList<>();

    public BadgeViewAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Badges> list) {
        super(context, 0,  list);
        mainContext = context;
        badgeList =  list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View item = convertView;
        if (item == null) {
            item = LayoutInflater.from(mainContext).inflate(R.layout.simple_badge_list_item, parent, false);
        }
        Badges badges = badgeList.get(position);

        ((TextView)item.findViewById(R.id.badge_text_view)).setText(badges.getTag());

        return item;
    }
}
