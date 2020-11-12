package com.bagguo.mydamai;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DaMaiAdapter extends RecyclerView.Adapter<DaMaiAdapter.DaMaiViewHolder> {


    public static final String TAG = DaMaiAdapter.class.getSimpleName();

    private final LayoutInflater inflater;
    private Context context;
    private ArrayList<String> data;

    public DaMaiAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DaMaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");

        View view = inflater.inflate(R.layout.item_topic_damai, parent, false);
        return new DaMaiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaMaiViewHolder holder, int position) {
        holder.itemTopicTitle.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    static class DaMaiViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_topic_icon)
        ImageView itemTopicIcon;
        @BindView(R.id.item_topic_title)
        TextView itemTopicTitle;

        public DaMaiViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
