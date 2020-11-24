package com.bagguo.mydamai.net;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wanggang on 2017/3/14.
 *
 * 拦截器：将要发出的请求进行拦截，然后改造再请求
 * 参数拦截器
 *
 */

public class BasicParamsInterceptor implements Interceptor {
    //chain 链 束缚

    @Override//使用链拦截
    public Response intercept(Chain chain) throws IOException {
            //请求
        Request request = chain.request();
        //拦截请求的url
        HttpUrl url = request.url();

        url = url.newBuilder()
                //插入键值对参数到 url query 中。
                .addQueryParameter("source", NetConfig.source)
                .addQueryParameter("appType", NetConfig.appType)
                .addQueryParameter("osType", NetConfig.osType)
                .addQueryParameter("version", NetConfig.version)
                .addQueryParameter("channel_from", NetConfig.channel_from)
                .build();
//1、构建请求体
        request=request.newBuilder()
                .url(url)
                .build();
//2、proceed开始；继续
        return chain.proceed(request);
    }
}
