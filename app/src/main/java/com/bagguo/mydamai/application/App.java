package com.bagguo.mydamai.application;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

//加快EventBus执行速度
//        EventBus.builder().addIndex(new MyEventBusIndex())
//                .installDefaultEventBus();
    }
}
