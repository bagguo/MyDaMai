package com.bagguo.mydamai.ui.topic.mvp.retrofit;

import com.bagguo.mydamai.ui.topic.FeedArticleBean;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

public interface ITopicRetrofitModel {
    Observable<List<FeedArticleBean>> getData1(HashMap<String, String> params);
    Observable<List<FeedArticleBean>> getData2(HashMap<String, String> params);
    Observable<List<FeedArticleBean>> getData3(HashMap<String, String> params);
}
