package com.bagguo.mydamai.ui.topic.mvp;

import android.util.Log;

import com.bagguo.mydamai.net.NetConfig;
import com.bagguo.mydamai.net.NetObserver;
import com.bagguo.mydamai.ui.topic.FeedArticleBean;

import java.util.List;

public class TopicPresenterImpl implements ITopicPresenter{

    public static final String TAG = TopicPresenterImpl.class.getSimpleName();

    private ITopicModel model;
    private ITopicView view;
    int page;

    public TopicPresenterImpl(ITopicView view) {
        this.view = view;
        model = new TopicModelImpl();
    }

    @Override
    public void loadData() {
        page = 1;
        String url = NetConfig.HOST + "article/list/" + page + "/json";

        view.showLoading();
        Log.d(TAG, String.valueOf(Thread.currentThread()));

        model.getDataFromNet(url)
                .subscribe(new NetObserver(view.getContext()) {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        view.hideLoading();
                        view.fillData((List<FeedArticleBean>) o, true);
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

        Log.d(TAG, String.valueOf(Thread.currentThread()));
        model.getDataFromNet(url)
                .subscribe(new NetObserver(view.getContext()) {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        view.hideLoading();
                        view.fillData((List<FeedArticleBean>) o, false);
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
