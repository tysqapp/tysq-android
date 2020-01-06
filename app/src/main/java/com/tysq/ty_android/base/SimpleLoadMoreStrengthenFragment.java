package com.tysq.ty_android.base;

import android.os.Bundle;

import com.bit.presenter.BasePresenter;
import com.bit.view.fragment.BitLoadMoreFragment;
import com.tysq.ty_android.base.activity.CommonToolbarStrengthenActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : liaozhenlin
 * time         : 2019/10/22 10:01
 * desc         : 可以改变标题栏按钮的封装上
 * version      : 1.5.0
 */
public abstract class SimpleLoadMoreStrengthenFragment <T extends BasePresenter, DATA>
        extends BitLoadMoreFragment<T>
        implements CommonToolbarStrengthenActivity.ICommonFragment {

    protected final static String IS_NEED_SWIPE = "IS_NEED_HEADER";
    protected final static String IS_NEED_REFRESH = "IS_NEED_HEADER";
    protected final static String IS_NEED_HEADER = "IS_NEED_HEADER";
    protected final static String USER_ID = "USER_ID";

    protected static final boolean IS_REFRESH = true;
    protected static final int PAGE_SIZE = 20;

    protected int userId;
    protected boolean isNeedHeader;
    protected boolean isNeedRefresh;
    protected boolean isNeedSwipe;

    protected final List<DATA> mData = new ArrayList<>();

    protected int size = 0;

    @Override
    public void getLoadMoreData() {
        loadData(size, PAGE_SIZE, false);
    }

    @Override
    public void getFirstData(int type) {
        this.size = 0;
        loadData(size, PAGE_SIZE, true);
    }

    /**
     * 加载数据
     *
     * @param start    开始的下标
     * @param pageSize 加载的长度
     * @param isFirst  是否为第一次
     */
    protected abstract void loadData(int start, int pageSize, boolean isFirst);

    @Override
    protected boolean requestRefresh() {
        return isNeedHeader;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        userId = arguments.getInt(USER_ID, -1);
        isNeedHeader = arguments.getBoolean(IS_NEED_HEADER, true);
        isNeedRefresh = arguments.getBoolean(IS_NEED_HEADER, IS_REFRESH);
        isNeedSwipe = arguments.getBoolean(IS_NEED_SWIPE, true);
    }

    /**
     * 处理返回的数据
     *
     * @param dataList 数据
     * @param isFirst  是否为第一次
     */
    public void onHandleResponseData(List<DATA> dataList, boolean isFirst) {

        if (isFirst) {
            mData.clear();

            if ((dataList == null || dataList.size() <= 0) && isNeedEmpty()) {
                mBaseAdapter.onEmpty();
                return;
            }

            mBaseAdapter.onSuccess();
        }

        // 没有数据 或是 没有数据不够 PAGE_SIZE 长度
        if (dataList == null
                || dataList.size() < PAGE_SIZE) {
            mBaseAdapter.setNoMore();
        } else {
            mBaseAdapter.setLoadComplete();
        }

        if (dataList != null) {
            mData.addAll(dataList);
            size += dataList.size();
        }

        mBaseAdapter.notifyDataSetChanged();

    }

    /**
     * 是否显示空白页
     */
    protected boolean isNeedEmpty() {
        return false;
    }

    /**
     * 处理错误情况
     *
     * @param isFirst 是否第一次
     */
    protected void onHandleError(boolean isFirst) {
        if (isFirst) {
            mBaseAdapter.onError();
        } else {
            mBaseAdapter.setLoadError();
        }
    }
}
