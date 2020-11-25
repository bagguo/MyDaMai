package com.bagguo.mydamai.ui.topic.mvp;

import com.alibaba.fastjson.JSONObject;
import com.bagguo.mydamai.cache.DiskCache;
import com.bagguo.mydamai.cache.DiskFunction;
import com.bagguo.mydamai.net.NetFunction;
import com.bagguo.mydamai.ui.topic.FeedArticleBean;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * model接口的实现类
 * 用于获取数据
 */
public class TopicModelImpl implements ITopicModel {

    @Override
    public Observable<List<FeedArticleBean>> getDataFromNet(String path) {
        /**
         * 网络获取数据，使用观察者模式
         *
         * .map(); 网络请求
         * .map解析数据
         */
        return Observable.just(path)
                .map(new NetFunction()) //网络请求
                .map(new TopicModelImpl.TopicFunction())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());//
    }

    @Override
    public Observable<List<FeedArticleBean>> getDataFromDisk(String path) {
        return Observable.just(path)
                .map(new DiskFunction())
                .map(new TopicFunction())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public boolean isTimeOut(String path, long timeOut) {
        return DiskCache.getInstance().isTimeout(path, timeOut);
    }

    //内部类 用于json解析
    static class TopicFunction implements Function<String, List<FeedArticleBean>> {
        @Override
        public List<FeedArticleBean> apply(@NonNull String s) throws Exception {
            //JSON 解析
            JSONObject object = JSONObject.parseObject(s);
            JSONObject data = object.getJSONObject("data");
            String list = data.getString("datas");
            List<FeedArticleBean> articles = JSONObject.parseArray(list, FeedArticleBean.class);
            return articles;
        }
    }

}
