package com.bagguo.mydamai.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bagguo.mydamai.application.App;

public class NetUtils {
    public static boolean isNetConnect() {
        ConnectivityManager mgr = (ConnectivityManager) App.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mgr.getActiveNetworkInfo();
        return networkInfo != null;
    }
}
