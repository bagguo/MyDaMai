package com.bagguo.mydamai.image;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.widget.ImageView;

import com.bagguo.mydamai.R;
import com.bumptech.glide.Glide;

public class ImageLoader {

    public static void load(Context context, String path, ImageView view) {
        Glide.with(context)
                .load(path)
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(view);
    }

    public static void load(Fragment context, String path, ImageView view) {
        Glide.with(context)
                .load(path)
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(view);
    }

    public static void load(Activity context, String path, ImageView view) {
        Glide.with(context)
                .load(path)
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(view);
    }
}
