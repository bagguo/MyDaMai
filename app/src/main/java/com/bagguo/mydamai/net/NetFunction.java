package com.bagguo.mydamai.net;

import com.bagguo.mydamai.cache.DiskCache;

import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetFunction implements Function<String, String> {
    @Override
    public String apply(String s) throws Exception {

        OkHttpClient client = NetRequest.getHttpClient();
        Request request = new Request.Builder()
                .url(s)
                .build();
        Response response = client.newCall(request).execute();

        String json = response.body().string();
        DiskCache.getInstance().set(s, json);
        return json;
    }
}
