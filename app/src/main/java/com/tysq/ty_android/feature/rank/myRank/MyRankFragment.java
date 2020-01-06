package com.tysq.ty_android.feature.rank.myRank;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.SimpleLoadMoreFragment;
import com.tysq.ty_android.base.SimpleLoadMoreStrengthenFragment;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.rank.myRank.di.DaggerMyRankComponent;
import com.tysq.ty_android.feature.rank.myRank.di.MyRankModule;
import com.tysq.ty_android.feature.web.TyWebViewActivity;
import com.tysq.ty_android.utils.TyUtils;

import java.lang.Override;

import response.rank.MyRankResp;
import response.rank.RankDetailResp;

/**
 * author       : frog
 * time         : 2019-07-15 18:22
 * desc         : 我的积分
 * version      : 1.3.0
 */

public final class MyRankFragment
        extends SimpleLoadMoreStrengthenFragment<MyRankPresenter, RankDetailResp.ScoresListBean>
        implements MyRankView {

    public static final String TAG = "MyRankFragment";

    private final MyRankResp myRankResp = new MyRankResp("0");

    public static MyRankFragment newInstance() {
        Bundle args = new Bundle();

        MyRankFragment fragment = new MyRankFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void registerDagger() {
        DaggerMyRankComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .myRankModule(new MyRankModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getTitleId() {
        return R.string.my_rank;
    }

    @Override
    public int getConfirmId() {
        return R.string.my_rank_des;
    }

    @Override
    public void setConfirmClick() {
        TyWebViewActivity.startActivity(getContext(),
                Constant.HtmlAPI.RANK_DETAIL);
    }

    @Override
    public int getConfirmBackground() {
        return 0;
    }

    @Override
    public int getConfirmTextColor() {
        return R.color.main_text_color;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new MyRankAdapter(getContext(), myRankResp, mData);
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.loadRankDetail(start, pageSize, isFirst);
    }

    @Override
    public void onLoadRankDetailFailure(boolean isFirst) {
        onHandleError(isFirst);
    }

    @Override
    public void onLoadRankDetail(RankDetailResp value,
                                 boolean isFirst) {
        // 设置积分
        myRankResp.setCount(TyUtils.formatDotNum(value.getTotalCount()));

        onHandleResponseData(value.getScoresList(), isFirst);
    }
}
