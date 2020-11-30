package com.bagguo.mydamai.ui.topic.mvp;

import com.alibaba.fastjson.JSONObject;
import com.bagguo.mydamai.db.SQLManager;
import com.bagguo.mydamai.greendao.gen.FeedArticleBeanDao;
import com.bagguo.mydamai.net.NetRequest;
import com.bagguo.mydamai.ui.topic.FeedArticleBean;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TopicModelDBImpl implements ITopicModel {

    @Override
    public Observable<List<FeedArticleBean>> getDataFromNet(String path) {
        return Observable.just(path)
                .map(new TopicModelDBImpl.TopicFunctionWithDb())//将数据插入数据库
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<FeedArticleBean>> getDataFromCache(String path) {
        return Observable.just(path)
                .map(new TopicDBFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public boolean isTimeOut(String path, long timeOut) {
        FeedArticleBeanDao topicDao = SQLManager.getInstance().getFeedDao();

        List<FeedArticleBean> list = topicDao.queryBuilder()
                .where(FeedArticleBeanDao.Properties.Key.eq(path))
                .limit(1)
                .list();

        if (list == null || list.size() == 0) {
            return true;//没有缓存数据
        }

        long time = list.get(0).getTime();
        return System.currentTimeMillis() - time > timeOut;
    }

    /**
     * 查询数据库
     */
    public static class TopicDBFunction implements Function<String, List<FeedArticleBean>> {

        @Override
        public List<FeedArticleBean> apply(String s) throws Exception {
            FeedArticleBeanDao dao = SQLManager.getInstance().getFeedDao();
            List<FeedArticleBean> list = dao.queryBuilder()
                    .where(FeedArticleBeanDao.Properties.Key.eq(s))
                    .list();
            return list;
        }
    }

    /**
     * 网路请求、插入数据库
     */
    public static class TopicFunctionWithDb implements Function<String, List<FeedArticleBean>> {

        @Override
        public List<FeedArticleBean> apply(@NonNull String s) throws Exception {

            //网络请求
            OkHttpClient client = NetRequest.getHttpClient();
            Request request = new Request.Builder()
                    .url(s)
                    .build();
            Response response = client.newCall(request).execute();
            String json = response.body().string();

            //JSON 解析
            JSONObject object = JSONObject.parseObject(json);
            JSONObject data = object.getJSONObject("data");
            String list = data.getString("datas");
            List<FeedArticleBean> articles = JSONObject.parseArray(list, FeedArticleBean.class);


            for (FeedArticleBean bean : articles) {
                bean.setKey(s);
                bean.setTime(System.currentTimeMillis());
            }

            FeedArticleBeanDao dao = SQLManager.getInstance().getFeedDao();

            List<FeedArticleBean> old = dao.queryBuilder()
                    .where(FeedArticleBeanDao.Properties.Key.eq(s))
                    .list();

            dao.deleteInTx(old);
            dao.insertOrReplaceInTx(articles);//插入数据库
            return articles;
        }
    }
}
