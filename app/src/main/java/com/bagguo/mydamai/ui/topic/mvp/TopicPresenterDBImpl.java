package com.bagguo.mydamai.ui.topic.mvp;

import com.bagguo.mydamai.cache.CacheObserver;
import com.bagguo.mydamai.cache.DiskObserver;
import com.bagguo.mydamai.net.NetConfig;
import com.bagguo.mydamai.net.NetObserver;
import com.bagguo.mydamai.ui.topic.FeedArticleBean;

import java.util.List;

public class TopicPresenterDBImpl implements ITopicPresenter {

    private ITopicModel model;
    private ITopicView view;
    private int page;

    public TopicPresenterDBImpl(ITopicView view) {
        this.view = view;
        model = new TopicModelDBImpl();
    }

    @Override
    public void loadData() {
        page = 1;
        String url = NetConfig.HOST + "article/list/" + page + "/json";

        //1.从本地获取
        model.getDataFromCache(url).subscribe(new CacheObserver<List<FeedArticleBean>>() {
            @Override
            public void onNext(List<FeedArticleBean> data) {
                view.fillData(data, true);
            }
        });

        //2.判断是否超时
        if (!model.isTimeOut(url, 60 * 1000)) {
            view.hideLoading();
            return;
        }

        //3.网络请求
        view.showLoading();
        model.getDataFromNet(url)
                .subscribe(new NetObserver<List<FeedArticleBean>>(view.getContext()) {
                    @Override
                    public void onNext(List<FeedArticleBean> data) {
                        super.onNext(data);
                        view.hideLoading();
                        view.fillData(data, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.hideLoading();
                        view.showError(e.getMessage());
                    }
                });
    }

    @Override
    public void addData() {
        page ++;
        String url = NetConfig.HOST + "article/list/" + page + "/json";

        //1.从本地获取数据
        model.getDataFromCache(url)
                .subscribe(new DiskObserver<List<FeedArticleBean>>() {
                    @Override
                    public void onNext(List<FeedArticleBean> data) {
                        view.fillData(data, false);
                    }
                });

        //2 判断是否超时
        if (!model.isTimeOut(url, 60 * 1000)) {
            view.hideLoading();
            return;
        }

        //3.网络请求
        view.showLoading();
        model.getDataFromNet(url)
                .subscribe(new NetObserver<List<FeedArticleBean>>(view.getContext()) {
                    @Override
                    public void onNext(List<FeedArticleBean> data) {
                        super.onNext(data);
                        view.hideLoading();
                        view.fillData(data, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.hideLoading();
                        view.showError(e.getMessage());
                    }
                });
    }
}
