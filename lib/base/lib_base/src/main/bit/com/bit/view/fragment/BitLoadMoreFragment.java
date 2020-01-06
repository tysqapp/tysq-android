package com.bit.view.fragment;

import android.support.v7.widget.RecyclerView;

import com.bit.presenter.BasePresenter;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;

/**
 * author       : frog
 * time         : 2019/3/26 下午3:15
 * desc         : 带上拉加载和刷新的fragment
 * version      : 1.3.0
 */
public abstract class BitLoadMoreFragment<T extends BasePresenter> extends BitListFragment<T> {

    protected void initAdapterForChild(RecyclerView.Adapter adapter) {

        this.mBaseAdapter.setIsOpenLoadMore(requestLoadMore());

        if (requestLoadMore()) {
            this.mBaseAdapter.setOnLoadMoreListener(new JRefreshAndLoadMoreAdapter.OnLoadMoreListener() {
                @Override
                public void onLoading() {
                    getLoadMoreData();
                }
            });
        }
    }

    /**
     * 是否需要刷新
     *
     * @return true: 需要；false: 不需要
     */
    @Override
    protected boolean requestRefresh() {
        return true;
    }

    /**
     * 是否需要加载更多
     *
     * @return true: 需要；false: 不需要
     */
    protected boolean requestLoadMore() {
        return true;
    }

    /**
     * 获取更多的数据
     */
    public abstract void getLoadMoreData();

}
