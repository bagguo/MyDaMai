package com.bagguo.mydamai.ui.topic.mvp.retrofit;

import com.bagguo.mydamai.net.NetObserver;
import com.bagguo.mydamai.ui.topic.FeedArticleBean;
import com.bagguo.mydamai.ui.topic.mvp.ITopicPresenter;
import com.bagguo.mydamai.ui.topic.mvp.ITopicView;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

public class TopicRetrofitPresenter implements ITopicPresenter {

    private int page;

//    public void register() {
//        EventBus.getDefault().register(this);
//    }
//
//    public void unRegister() {
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(Event event) {
//        switch (event.getType()) {
//            case Event.LIST_TOPIC_OK:
//                List<FeedArticleBean> data = (List<FeedArticleBean>) event.getData();
//                break;
//
//            case Event.LIST_TOPIC_ERROR:
//                String msg = (String) event.getData();
//                break;
//        }
//    }

    private ITopicView view;
    private ITopicRetrofitModel model;

    public TopicRetrofitPresenter(ITopicView view) {
        this.view = view;
        model = new TopicRetrofitModelImpl();
    }

    @Override
    public void loadData() {
        view.showLoading();
        page = 1;

        HashMap<String, String> params = new HashMap<>();
//        params.put("cc", "0");
//        params.put("ps", "20");
//        params.put("mc", "0");
//        params.put("ot", "0");
//        params.put("v", "0");
//        params.put("p", page + "");
//        params.put("cityId", "852");


        //第一个接口
//        Observable<List<FeedArticleBean>> observable = model.getData1(params);
        Observable<List<FeedArticleBean>> observable = model.getData2(params);
//        Observable<List<FeedArticleBean>> observable = model.getData1(params);

        observable.subscribe(new NetObserver<List<FeedArticleBean>>(view.getContext()) {
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
        view.showLoading();
        page++;

        HashMap<String, String> params = new HashMap<>();
//        params.put("cc", "0");
//        params.put("ps", "20");
//        params.put("mc", "0");
//        params.put("ot", "0");
//        params.put("v", "0");
//        params.put("p", page + "");
//        params.put("cityId", "852");
        model.getData1(params).subscribe(new NetObserver<List<FeedArticleBean>>(view.getContext()) {
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
