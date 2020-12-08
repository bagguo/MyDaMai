package com.bagguo.mydamai.utils;

import android.widget.Toast;

import com.bagguo.mydamai.application.App;

public class ToastUtils {

    private static Toast toast;

    public static void toast(String message) {
        if (toast == null) {
            toast = Toast.makeText(App.getContext(), "", Toast.LENGTH_LONG);
        }
        toast.setText(message);
        toast.show();
    }
}
