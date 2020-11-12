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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class DaMaiFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = DaMaiFragment.class.getSimpleName();

    @BindView(R.id.topic_recycle)
    RecyclerView topicRecycle;
    @BindView(R.id.topic_swipe)
    SwipeRefreshLayout topicSwipe;


    private DaMaiAdapter adapter;
    private ArrayList<FeedArticleBean> data = new ArrayList<>();

    private DaMaiHandler handler;

    public DaMaiFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        View view = inflater.inflate(R.layout.fragment_damai, container, false);
        ButterKnife.bind(this, view);
        handler = new DaMaiHandler(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();
        if (context == null) return;

        topicSwipe.setColorSchemeColors(
                Color.CYAN,
                Color.YELLOW,
                Color.GREEN,
                Color.RED);
        topicSwipe.setOnRefreshListener(this);

        LinearLayoutManager mgr = new LinearLayoutManager(context);
        topicRecycle.setLayoutManager(mgr);


        DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        topicRecycle.addItemDecoration(decoration);

        adapter = new DaMaiAdapter(context, data);
        topicRecycle.setAdapter(adapter);


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
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
                .url(NetConfig.HOST + "article/list/0/json"


                        /*"http://mapi.damai.cn/" +
                        "ProjLst.aspx?" +
                        "source=10101&" +
                        "cc=0&" +
                        "ps=20&" +
                        "mc=0&" +
                        "ot=0&" +
                        "v=0&" +
                        "appType=1&" +
                        "osType=2&" +
                        "p=1&" +
                        "version=50609&" +
                        "channel_from=xiaomi_market&" +
                        "cityId=852"*/)
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

                /**
                 * 响应完成通过handler将数据发给handler进行UI更新
                 */
                Message message = handler.obtainMessage();
                message.obj = topics;
                message.what = 200;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    public void fillData(List<FeedArticleBean> data) {

        this.data.clear();
        this.data.addAll(data);
        adapter.notifyDataSetChanged();
    }

    private void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }


    static class DaMaiHandler extends Handler {
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
    }
}
