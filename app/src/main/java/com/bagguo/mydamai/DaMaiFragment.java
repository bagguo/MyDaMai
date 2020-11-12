package com.bagguo.mydamai;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DaMaiFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = DaMaiFragment.class.getSimpleName();

    @BindView(R.id.topic_recycle)
    RecyclerView topicRecycle;
    @BindView(R.id.topic_swipe)
    SwipeRefreshLayout topicSwipe;


    private ArrayList<String> data = new ArrayList<>();

    public DaMaiFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        View view = inflater.inflate(R.layout.fragment_damai, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();
        if (context == null) return;

        topicSwipe.setColorSchemeColors(
                Color.CYAN,
                Color.YELLOW,
                Color.GREEN,
                Color.RED);
        topicSwipe.setOnRefreshListener(this);

        LinearLayoutManager mgr = new LinearLayoutManager(context);
        topicRecycle.setLayoutManager(mgr);


        DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        topicRecycle.addItemDecoration(decoration);

        for (int i = 0; i < 10; i++) {
            data.add("6789kk");
        }

        DaMaiAdapter adapter = new DaMaiAdapter(context, data);
        topicRecycle.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {

    }
}
