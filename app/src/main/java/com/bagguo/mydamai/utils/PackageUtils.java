package com.bagguo.mydamai.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.bagguo.mydamai.application.App;


/**
 * Created by wanggang on 2017/3/14.
 */

public class PackageUtils {
//得到版本号
    public static int getVersionCode(){
        //app得到上下文，得到包管理器
        PackageManager pm = App.getContext().getPackageManager();//context为当前Activity上下文
        PackageInfo pi = null;
        try {
            //通过包管理器得到包信息
            pi = pm.getPackageInfo(App.getContext().getPackageName(), 0);
            return pi.versionCode;//返回包信息的版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

}
