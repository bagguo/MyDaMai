package com.bagguo.mydamai.net;

import android.util.Log;

import com.bagguo.mydamai.utils.FileUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetRequest {

    public static final String TAG = NetRequest.class.getSimpleName();

    public static OkHttpClient client;
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (NetRequest.client) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(NetConfig.HOST)
                            .client(NetRequest.getHttpClient())
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    public static OkHttpClient getHttpClient() {

        if (client != null) {
            return client;
        }

        synchronized (NetRequest.class) {
            if (client != null) {
                return client;
            }

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d(TAG, "log: ======" + message);
                }
            });
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


            final Cache cache = new Cache(FileUtils.getNetCacheFloder(), 30 * 1024 * 1024);

            client = new OkHttpClient.Builder()
                    .cache(cache)//缓存文件路径
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(new CacheInterceptor())
//                    .addInterceptor(new BasicParamsInterceptor())//添加公共参数
                    .addInterceptor(interceptor)
                    .build();
        }
        return client;
    }
}
