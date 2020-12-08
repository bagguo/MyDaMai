package com.bagguo.mydamai.ui.topic.mvp.retrofit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bagguo.mydamai.net.IApi;
import com.bagguo.mydamai.net.NetConfig;
import com.bagguo.mydamai.net.NetRequest;
import com.bagguo.mydamai.ui.topic.FeedArticalListBean;
import com.bagguo.mydamai.ui.topic.FeedArticleBean;
import com.bagguo.mydamai.ui.topic.TopicBean;
import com.bagguo.mydamai.ui.topic.mvp.TopicPresenterDBImpl;
import com.bagguo.mydamai.utils.Event;



import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TopicRetrofitModelImpl implements ITopicRetrofitModel{

    private final IApi iApi;

    public TopicRetrofitModelImpl(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConfig.HOST)
                .client(NetRequest.getHttpClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        this.iApi = retrofit.create(IApi.class);
    }

    //回调方式
    public interface OnNetResponse{
        void onResponse(Object data);
        void onFail(String error);
    }

    public void getData(HashMap<String, String> params, final OnNetResponse callBack) {
        Call<ResponseBody> call = iApi.listTopic1(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    FeedArticalListBean bean = JSONObject.parseObject(json, FeedArticalListBean.class);

                    //或者使用EventBus把结果回调
                    if (callBack != null) {
                        callBack.onResponse(bean.getL());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    if (callBack != null) {
                        callBack.onFail(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (callBack != null) {
                    callBack.onFail(t.getMessage());
                }
            }
        });
    }

//    public void getDataEventBus(HashMap<String, String> params) {
//        Call<ResponseBody> call = iApi.listTopic1(params);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    String json = response.body().string();
//                    FeedArticalListBean listBean = JSONObject.parseObject(json, FeedArticalListBean.class);
//                    EventBus.getDefault().post(new Event(Event.LIST_TOPIC_OK, listBean));
//                 } catch (IOException e) {
//                    e.printStackTrace();
//                    EventBus.getDefault().post(new Event(Event.LIST_TOPIC_ERROR, e.getMessage()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                EventBus.getDefault().post(new Event(Event.LIST_TOPIC_ERROR, t.getMessage()));
//            }
//        });
//    }

    @Override
    public Observable<List<FeedArticleBean>> getData1(HashMap<String, String> params) {
//        call.execute()同步
//        call.enqueue();异步

        //被观察者
        return Observable.just(params)
                .map(new Function<HashMap<String, String>, List<FeedArticleBean>>() {
                    @Override
                    public List<FeedArticleBean> apply(HashMap<String, String> params) throws Exception {
                        Call<ResponseBody> call = iApi.listTopic1(params);
                        Response<ResponseBody> response = call.execute();
                        String json = response.body().string();
                        FeedArticalListBean listBean = JSONObject.parseObject(json, FeedArticalListBean.class);
                        List<FeedArticleBean> l = listBean.getL();
                        return l;
                    }

                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<FeedArticleBean>> getData2(HashMap<String, String> params) {
        return Observable.just(params)
                .map(new Function<HashMap<String, String>, List<FeedArticleBean>>() {
                    @Override
                    public List<FeedArticleBean> apply(HashMap<String, String> params) throws Exception {

                        Call<FeedArticalListBean> call = iApi.listTopic3(params);
                        Response<FeedArticalListBean> response = call.execute();
                        return response.body().getL();
                    }
                });
    }

    @Override
    public Observable<List<FeedArticleBean>> getData3(HashMap<String, String> params) {
        return iApi.listTopic4(params)
                .map(new Function<FeedArticalListBean, List<FeedArticleBean>>() {
                    @Override
                    public List<FeedArticleBean> apply(@NonNull FeedArticalListBean list) throws Exception {
                        return list.getL();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
