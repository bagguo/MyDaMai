package com.bagguo.mydamai.cache;

import com.bagguo.mydamai.utils.FileUtils;
import com.bagguo.mydamai.utils.MD5Utils;
import com.bagguo.mydamai.utils.PackageUtils;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;


public class DiskCache {

    private static DiskCache instance;

    private DiskLruCache diskLruCache;

    public static DiskCache getInstance() {

        if (instance != null) {
            return instance;
        }

        synchronized (DiskCache.class) {
            if (instance != null) {
                return instance;
            }

            instance = new DiskCache();
        }
        return instance;
    }

    private DiskCache() {
        File cacheFloder = FileUtils.getJsonCacheFloder();

        int versionCode = PackageUtils.getVersionCode();

        int maxSize = 30 * 1024 * 1024;

        try {
            this.diskLruCache = DiskLruCache.open(cacheFloder, versionCode, 2, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 存储，进行缓存
     * 存值，记录缓存的时间
     *
     * @param k
     * @param v
     */
    public void set(String k, String v) {
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(getCacheKey(k));
            editor.set(0, v);
            editor.set(1, System.currentTimeMillis() + "");//存时间
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 取缓存
     * 得到json
     *
     * @param k
     * @return
     */
    public String get(String k) {
        try {
            //由加密后的key得到lrucache快照
            //根据key值得到缓存key，再得到缓存内容
            //snapchat快照
            DiskLruCache.Snapshot snapshot = diskLruCache.get(getCacheKey(k));
            if (snapshot == null) return "";

            //第一个位置的数据为json
            String json = snapshot.getString(0);
            snapshot.close();//关闭缓存
            return json;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 是否超时
     * 设定一个时间timeOut，超时这个时间就不从缓存中取数据，直接从网络
     * 缓存时的时间timemills
     *
     * @param k
     * @param timeOut
     * @return
     */
    public boolean isTimeout(String k, long timeOut) {
        try {
            //根据key得到缓存
            DiskLruCache.Snapshot snapshot = diskLruCache.get(getCacheKey(k));
            if (snapshot == null)
                return true;
            //第二个数据是超时时间
            String time = snapshot.getString(1);
            snapshot.close();
            //将字符串解析为long类型的毫秒
            long timeMills = Long.parseLong(time);
            // 当前的时间，以毫秒计算
            return System.currentTimeMillis() - timeMills > timeOut;//是否超时
        } catch (IOException e) {

        }
        return true;
    }

    private String getCacheKey(String k) {
        return MD5Utils.hashKeyForDisk(k);
    }
}
