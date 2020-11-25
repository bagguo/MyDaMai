package com.bagguo.mydamai.ui.topic.mvp;

import com.bagguo.mydamai.ui.topic.FeedArticleBean;
import com.bagguo.mydamai.ui.topic.TopicBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * mvp
 * m 加载数据
 * net, disk, sql, sp
 */
public interface ITopicModel {

    //网络获取
    Observable<List<FeedArticleBean>> getDataFromNet(String path);

    //从缓存中获取数据
    Observable<List<FeedArticleBean>> getDataFromDisk(String path);

    boolean isTimeOut(String path, long timeOut);
}
