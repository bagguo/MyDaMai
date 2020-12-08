package com.bagguo.mydamai.utils;

import android.content.Context;
import android.os.Environment;

import com.bagguo.mydamai.application.App;

import java.io.File;

public class FileUtils {

    private static File getCacheRootFolder() {
        Context context = App.getContext();
        if (Environment.isExternalStorageEmulated()) {
            return context.getExternalCacheDir();
        } else {
            return context.getCacheDir();
        }
    }

    public static File getJsonCacheFloder() {
        //得到缓存根文件夹
        File rootFiloder = getCacheRootFolder();
        //在根文件夹创建json的缓存文件
        File cache = new File(rootFiloder, "json");
        //不存在就创建
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cache;
    }

    public static File getDnsCacheFolder() {
        File rootFolder = getCacheRootFolder();
        File cache = new File(rootFolder, "dns");
        if (!cache.exists()) {
            cache.mkdir();
        }
        return cache;
    }

    public static File getNetCacheFloder() {
        File rootFiloder = getCacheRootFolder();
        File cache=new File(rootFiloder,"net");
        if(!cache.exists()){
            cache.mkdirs();
        }
        return cache;
    }
}
