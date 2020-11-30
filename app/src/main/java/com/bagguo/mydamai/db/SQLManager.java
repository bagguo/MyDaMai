package com.bagguo.mydamai.db;

import com.bagguo.mydamai.application.App;
import com.bagguo.mydamai.greendao.gen.DaoMaster;
import com.bagguo.mydamai.greendao.gen.DaoSession;
import com.bagguo.mydamai.greendao.gen.FeedArticleBeanDao;

public class SQLManager {

    private static SQLManager instance;
    private final DaoSession daoSession;

    public static SQLManager getInstance() {
        if (instance == null) {
            synchronized (SQLManager.class) {
                instance = new SQLManager();
            }
        }
        return instance;
    }

    private SQLManager() {
        DaoMaster.DevOpenHelper openHelper =
                new DaoMaster.DevOpenHelper(App.getContext(), "data.db");

        DaoMaster master = new DaoMaster(openHelper.getWritableDb());
        daoSession = master.newSession();
    }

    public FeedArticleBeanDao getFeedDao() {
        return daoSession.getFeedArticleBeanDao();
    }
}
