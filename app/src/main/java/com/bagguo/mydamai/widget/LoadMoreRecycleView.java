package com.bagguo.mydamai.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wanggang on 2017/3/14.
 * 加载更多的recyclerview继承recyclerview
 */
public class LoadMoreRecycleView extends RecyclerView {

    public LoadMoreRecycleView(@NonNull Context context) {
        super(context);
        init();
    }

    public LoadMoreRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //闲置
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    if (getAdapter() == null) return;

                    //是否需要加载更多
                    if (lastPosition == getAdapter().getItemCount() - 1) {
                        //上拉加载，判断是否是加载中的状态，
                        //如果正在加载，则不会再去加载
                        if (!isRefreshing && l != null) {
                            isRefreshing = true;//添加数据
                            l.loadMore();
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
        });
    }
    //
    private boolean isRefreshing;

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setRefreshing(boolean refreshing) {
        this.isRefreshing = refreshing;
    }


    private OnLoadMoreListener l;

    public void setOnLoadMoreListener(OnLoadMoreListener l) {
        this.l = l;
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }
}
