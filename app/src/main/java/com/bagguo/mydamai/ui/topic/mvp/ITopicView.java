package com.bagguo.mydamai.ui.topic.mvp;

import android.content.Context;

import com.bagguo.mydamai.ui.topic.FeedArticleBean;
import com.bagguo.mydamai.ui.topic.TopicBean;

import java.util.List;

/**
 * Created by wanggang on 2017/3/14.
 * <p>
 * MPV
 * v  view 控制ui界面的展示
 * 加载中的效果
 * 加载失败的效果
 * 加载完成的效果
 * 显示错误提示
 * 显示操作提示.....
 */
public interface ITopicView {

    void fillData(List<FeedArticleBean> data, boolean isPull);//填充数据

    void showError(String error);//展示错误

    void showLoading();//展示加载

    void hideLoading();//隐藏加载

    Context getContext();
}
