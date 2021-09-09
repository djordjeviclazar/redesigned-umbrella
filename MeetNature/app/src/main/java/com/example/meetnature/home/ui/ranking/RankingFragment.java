package com.example.meetnature.home.ui.ranking;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.meetnature.MainActivity;
import com.example.meetnature.R;
import com.example.meetnature.controllers.UserController;
import com.example.meetnature.data.models.User;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankingFragment extends Fragment {
    public RankingFragment() {
        // Required empty public constructor
    }

    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.ranking_fragmanet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView rankingListLayout = view.findViewById(R.id.ranking_users_list);
        UserController.getInstance().orderUsersByScore(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Stack<User> userStack = (Stack<User>) o;
                ArrayList<User> usersOrdered = new ArrayList<>();
                while (!userStack.empty()){
                    usersOrdered.add(userStack.pop());
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    usersOrdered.sort(new Comparator<User>() {
                        @Override
                        public int compare(User o1, User o2) {
                            if (o1.getScore() > o2.getScore())
                                return -1;
                            if (o2.getScore() > o1.getScore())
                                return 1;
                            return 0;
                        }
                    });
                }

                RankingAdapter rankingAdapter = new RankingAdapter(getContext(), usersOrdered, (MainActivity) getActivity());
                rankingListLayout.setAdapter(rankingAdapter);
            }
        });
    }
}