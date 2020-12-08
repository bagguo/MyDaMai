package com.bagguo.mydamai.net;

import com.bagguo.mydamai.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpDns implements Dns {
    //dns客户端
    private static OkHttpClient httpDnsClient;


    @Override               //查找、检查
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        //防御代码
        if (hostname == null) throw new UnknownHostException("hostname == null");

        //dnspod 提供的dns服务
        HttpUrl httpUrl = new HttpUrl.Builder().scheme("http")
                .host("119.29.29.29")
                .addPathSegment("d")//段落
                .addQueryParameter("dn", hostname)
                .build();
        Request dnsRequest = new Request.Builder().url(httpUrl).get().build();

        try {
            String s = getHttpDnsClient().newCall(dnsRequest).execute().body().string();
            //避免服务器挂了却无法查询DNS
            if (!s.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b")) {
                return Dns.SYSTEM.lookup(hostname);
            }
            return Arrays.asList(InetAddress.getAllByName(s));
        } catch (IOException e) {
            return Dns.SYSTEM.lookup(hostname);
        }
    }

    public static synchronized OkHttpClient getHttpDnsClient() {
        if (httpDnsClient == null) {
            File dnsCacheFolder = FileUtils.getDnsCacheFolder();
            httpDnsClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response originalResponse = chain.proceed(chain.request());
                            return originalResponse.newBuilder()
                                    //在返回header中加入缓存消息
                                    //下次将不再发送请求
                                    .header("Cache-Control", "max-age=600").build();
                        }
                    })
            .cache(new Cache(dnsCacheFolder, 5 * 1024 * 1024))
            .build();
        }
        return httpDnsClient;
    }
}
