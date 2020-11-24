package com.bagguo.mydamai.ui.topic;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bagguo.mydamai.R;
import com.bagguo.mydamai.net.NetConfig;
import com.bagguo.mydamai.net.NetFunction;
import com.bagguo.mydamai.net.NetObserver;
import com.bagguo.mydamai.ui.topic.mvp.ITopicView;
import com.bagguo.mydamai.ui.topic.mvp.TopicPresenterImpl;
import com.bagguo.mydamai.widget.LoadMoreRecycleView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class DaMaiFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener, ITopicView, LoadMoreRecycleView.OnLoadMoreListener {

    public static final String TAG = DaMaiFragment.class.getSimpleName();

    @BindView(R.id.topic_recycle)
    LoadMoreRecycleView topicRecycle;
    @BindView(R.id.topic_swipe)
    SwipeRefreshLayout topicSwipe;


    private Context mContext;
    private DaMaiAdapter adapter;
    private ArrayList<FeedArticleBean> data = new ArrayList<>();
    private TopicPresenterImpl topicPresenter;

//    private DaMaiHandler handler;

    public DaMaiFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        View view = inflater.inflate(R.layout.fragment_damai, container, false);
        ButterKnife.bind(this, view);
//        handler = new DaMaiHandler(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        if (mContext == null) return;

        topicSwipe.setColorSchemeColors(
                Color.CYAN,
                Color.YELLOW,
                Color.GREEN,
                Color.RED);
        topicSwipe.setOnRefreshListener(this);

        LinearLayoutManager mgr = new LinearLayoutManager(mContext);
        topicRecycle.setLayoutManager(mgr);

        DividerItemDecoration decoration =
                new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        topicRecycle.addItemDecoration(decoration);

        adapter = new DaMaiAdapter(mContext, data);
        topicRecycle.setAdapter(adapter);


        topicRecycle.setOnLoadMoreListener(this);

        topicPresenter = new TopicPresenterImpl(this);
        topicPresenter.loadData();
    }
/*topicRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //闲置
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //是否需要加载更多
                    if (lastPosition == data.size() - 1) {
                        //上拉加载，判断是否是加载中的状态，
                        //如果正在加载，则不会再去加载
                        if (!topicSwipe.isRefreshing()) {
                            addData();//添加数据
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //得到recyclerview的布局管理器
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                //实例化线性布局管理器
                if (manager instanceof LinearLayoutManager) {
                    //布局管理器找到最后一个可见的条目位置赋值给最后一个可见的条目
                    int lastVisibleItemPosition = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                    if (lastVisibleItemPosition != -1) {
                        //得到最后一个条目
                        lastPosition = lastVisibleItemPosition;
                    }
                }
            }
        });*/

//        loadData();
    /*int page;
    private void loadData() {
        page = 0;
        String url = NetConfig.HOST + "article/list/" + page + "/json";

        //被观察者
        Observable.just(url)
                .map(new NetFunction())//网络请求 ,
                .map(new Function<String, List<FeedArticleBean>>() {//解析
                    @Override
                    public List<FeedArticleBean> apply(String s) throws Exception {

                        JSONObject object = JSONObject.parseObject(s);
                        JSONObject data = object.getJSONObject("data");
                        String list = data.getString("datas");
                        List<FeedArticleBean> articles = JSONObject.parseArray(list, FeedArticleBean.class);
                        return articles;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//观察者指定主线程
                .subscribeOn(Schedulers.io())//订阅
                .subscribe(new NetObserver(mContext) {//订阅 观察者，处理相应动作
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        fillData((List<FeedArticleBean>) o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        showError(e.getMessage());
                    }
                });
    }*/


    @Override
    public void onRefresh() {
        topicPresenter.loadData();
    }


    /**
     * LoadMoreRecyclerview 中声明的接口
     */
    @Override
    public void loadMore() {
        topicPresenter.addData();
    }

    @Override
    public void showError(String error) {
        topicSwipe.setRefreshing(false);
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        topicSwipe.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        topicSwipe.setRefreshing(false);
    }

    @Override
    public void fillData(List<FeedArticleBean> data, boolean isPull) {

        if (isPull) {//下拉刷新清除之前的数据
            this.data.clear();
        } else {
            topicRecycle.setRefreshing(false);
        }
        this.data.addAll(data);
        adapter.notifyDataSetChanged();
        topicSwipe.setRefreshing(false);
    }

    /**
     * 不用mvp只用观察者模式
     */
        /*private void addData() {
        page++;
        topicSwipe.setRefreshing(true);
        String url = NetConfig.HOST + "article/list/" + page + "/json";

        //被观察者
        Observable.just(url)
                .map(new NetFunction())//网络请求 ,
                .map(new Function<String, List<FeedArticleBean>>() {//解析
                    @Override
                    public List<FeedArticleBean> apply(String s) throws Exception {

                        JSONObject object = JSONObject.parseObject(s);
                        JSONObject data = object.getJSONObject("data");
                        String list = data.getString("datas");
                        List<FeedArticleBean> articles = JSONObject.parseArray(list, FeedArticleBean.class);
                        return articles;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//观察者指定主线程
                .subscribeOn(Schedulers.io())//订阅
                .subscribe(new NetObserver(mContext) {//订阅 观察者，处理相应动作
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        fillData((List<FeedArticleBean>) o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        showError(e.getMessage());
                    }
                });
    }

    public void fillData(List<FeedArticleBean> data) {

        this.data.clear();
        this.data.addAll(data);
        adapter.notifyDataSetChanged();
        topicSwipe.setRefreshing(false);

    }*/



    /*static class DaMaiHandler extends Handler {
        private WeakReference<DaMaiFragment> weakReference;

        public DaMaiHandler(DaMaiFragment fragment) {
            this.weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 100:
                    weakReference.get().showError(msg.obj.toString());
                    break;
                case 200:
                    List<FeedArticleBean> list = (ArrayList<FeedArticleBean>) msg.obj;
                    weakReference.get().fillData(list);
                    break;
            }
        }
    }*/
    /**
     * 请求方式1
     * 使用okhttp请求网络；请求后将响应结果用handler发送至handler进行UI更新
     */
       /* HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i(TAG, "log: ======" + message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        final Request request = new Request.Builder()
                .url(url
//                        "http://mapi.damai.cn/" +
//                        "ProjLst.aspx?" +
//                        "source=10101&" +
//                        "cc=0&" +
//                        "ps=20&" +
//                        "mc=0&" +
//                        "ot=0&" +
//                        "v=0&" +
//                        "appType=1&" +
//                        "osType=2&" +
//                        "p=1&" +
//                        "version=50609&" +
//                        "channel_from=xiaomi_market&" +
//                        "cityId=852"
                )
                .build();

        //queue
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message message = handler.obtainMessage();
                message.obj = e.getMessage();
                message.what = 100;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String respStr = response.body().string();
                JSONObject object = JSONObject.parseObject(respStr);
                JSONObject data = object.getJSONObject("data");
                String list = data.getString("datas");
                List<FeedArticleBean> topics = JSONObject.parseArray(list, FeedArticleBean.class);

                // 响应完成通过handler将数据发给handler进行UI更新

                Message message = handler.obtainMessage();
                message.obj = topics;
                message.what = 200;
                handler.sendMessage(message);
            }
        });*/

    /**
     * 请求方式2
     * rxjava + okhttp
     *
     * 1.被观察者发布事件流 implements Function<String, String>.apply(String s)
     * 2.事件完成后 观察者 订阅收到响应结果 implements Observer {
     *     onSubscribe(Disposable d) {}
     *     onNext(Object o) {}
     *     onError(Throwable e) {}
     *     onComplete() {}
     * }
     */
    //===========
    //RxJava  观察者模式 样例
    //Observable   被观察者    按钮，当按钮被点击，就触发了观察者的操作
    //Observer     观察者      Listener

//    Observable<String> observable = Observable.just("url")
//            .map(new Function<String, String>() {
//                @Override
//                public String apply(@NonNull String s) throws Exception {
//                    //下载操作
//                    return s;
//                }
//            });
//
//
//
//    Observer<String> observer=new Observer<String>() {
//        @Override
//        public void onSubscribe(Disposable d) { }
//
//        @Override
//        public void onNext(String s) { }
//
//        @Override
//        public void onError(Throwable e) { }
//
//        @Override
//        public void onComplete() { }
//    };
//
//        observable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(observer);
}
