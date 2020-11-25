package com.bagguo.mydamai.ui.topic.mvp;

import android.text.TextUtils;
import android.util.Log;

import com.bagguo.mydamai.cache.DiskCache;
import com.bagguo.mydamai.cache.DiskObserver;
import com.bagguo.mydamai.net.NetConfig;
import com.bagguo.mydamai.net.NetObserver;
import com.bagguo.mydamai.ui.topic.FeedArticleBean;

import java.util.List;

public class TopicPresenterImpl implements ITopicPresenter{

    public static final String TAG = TopicPresenterImpl.class.getSimpleName();

    private final DiskCache diskCache;
    private ITopicModel model;
    private ITopicView view;
    int page;

    public TopicPresenterImpl(ITopicView view) {
        this.view = view;
        model = new TopicModelImpl();
        this.diskCache = DiskCache.getInstance();
    }

    @Override
    public void loadData() {
        page = 1;
        String url = NetConfig.HOST + "article/list/" + page + "/json";

        Log.d(TAG, String.valueOf(Thread.currentThread()));

        //1 从本地取数据
        /*        String json = diskCache.get(url);
        if (!TextUtils.isEmpty(json)) {
            List<FeedArticleBean> data = null;

            try {
                data = new TopicModelImpl
                        .TopicFunction()
                        .apply(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            view.fillData(data, true);
        }*/
        model.getDataFromDisk(url).subscribe(new DiskObserver<List<FeedArticleBean>>() {
            @Override
            public void onNext(List<FeedArticleBean> data) {
                view.fillData(data, true);
            }
        });

        //2 判断是否超时
        if (!model.isTimeOut(url, 60 * 1000)) {
            view.hideLoading();
            return;
        }

        //3 网球请求
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

        Log.d(TAG, String.valueOf(Thread.currentThread()));

        //1 从本地取数据
        model.getDataFromDisk(url).subscribe(new DiskObserver<List<FeedArticleBean>>() {
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

        //3 网球请求
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
