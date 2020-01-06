package com.tysq.ty_android.feature.rank.orderRank;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.SimpleLoadMoreFragment;
import com.tysq.ty_android.feature.rank.orderRank.di.DaggerOrderRankComponent;
import com.tysq.ty_android.feature.rank.orderRank.di.OrderRankModule;

import java.lang.Override;

import response.rank.RankOrderResp;

/**
 * author       : frog
 * time         : 2019-07-15 17:42
 * desc         : 积分订单
 * version      : 1.3.0
 */
public final class OrderRankFragment
        extends SimpleLoadMoreFragment<OrderRankPresenter, RankOrderResp.ScoresorderListBean>
        implements OrderRankView {

    public static final String TAG = "OrderRankFragment";

    public static OrderRankFragment newInstance() {

        Bundle args = new Bundle();

        OrderRankFragment fragment = new OrderRankFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void registerDagger() {
        DaggerOrderRankComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .orderRankModule(new OrderRankModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new OrderRankAdapter(getContext(), mData);
    }


    @Override
    public int getTitleId() {
        return R.string.rank_order;
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.getScoreOrder(start, pageSize, isFirst);
    }

    @Override
    public void onGetScoreOrderFailure(boolean isFirst) {
        onHandleError(isFirst);
    }

    @Override
    public void onGetScoreOrder(RankOrderResp value, boolean isFirst) {
        onHandleResponseData(value.getScoresorderList(), isFirst);
    }
}
