package com.tysq.ty_android.feature.coin.coinWithdrawLog;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.coin.coinWithdrawLog.di.CoinWithdrawLogModule;
import com.tysq.ty_android.feature.coin.coinWithdrawLog.di.DaggerCoinWithdrawLogComponent;
import com.tysq.ty_android.base.SimpleLoadMoreFragment;

import java.lang.Override;
import java.util.List;

import response.coin.WithdrawLogResp;

/**
 * author       : frog
 * time         : 2019-08-13 11:33
 * desc         : 提币记录
 * version      : 1.3.0
 */
public final class CoinWithdrawLogFragment
        extends SimpleLoadMoreFragment<CoinWithdrawLogPresenter, WithdrawLogResp.WithdrawReviewListBean>
        implements CoinWithdrawLogView {

    public static final String TAG = "CoinWithdrawLogFragment";

    public static CoinWithdrawLogFragment newInstance() {
        Bundle args = new Bundle();

        CoinWithdrawLogFragment fragment = new CoinWithdrawLogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void registerDagger() {
        DaggerCoinWithdrawLogComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .coinWithdrawLogModule(new CoinWithdrawLogModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.getWithdrawLog(start, pageSize, isFirst);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CoinWithdrawLogAdapter(getContext(), mData);
    }

    @Override
    public int getTitleId() {
        return R.string.withdraw_log;
    }


    @Override
    public void onGetWithdrawLogError(boolean isFirst) {
        onHandleError(isFirst);
    }

    @Override
    public void onGetWithdrawLog(boolean isFirst,
                                 List<WithdrawLogResp.WithdrawReviewListBean> withdrawReviewList) {
        onHandleResponseData(withdrawReviewList, isFirst);
    }

    @Override
    protected boolean isNeedEmpty() {
        return true;
    }

    @Override
    protected int getEmptyView() {
        return R.layout.blank_empty_withdraw_log;
    }
}
