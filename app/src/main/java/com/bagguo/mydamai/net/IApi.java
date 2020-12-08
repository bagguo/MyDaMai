package com.bagguo.mydamai.net;

import com.bagguo.mydamai.ui.topic.FeedArticalListBean;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface IApi {
    //        String url = NetConfig.HOST + "article/list/" + page + "/json";
    @GET("article/list/")
    Call<ResponseBody> listTopic1(@QueryMap HashMap<String, String> map);

    @GET("article/list/")
    Call<ResponseBody> listTopic2(@QueryMap HashMap<String, String> map);

    @GET("article/list/")
    Call<FeedArticalListBean> listTopic3(@QueryMap HashMap<String, String> map);

    @GET("article/list/")
    Observable<FeedArticalListBean> listTopic4(@QueryMap HashMap<String, String> map);
}
