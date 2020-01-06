package com.tysq.ty_android.feature.coin.myCoin;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.SimpleLoadMoreStrengthenFragment;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.coin.myCoin.di.DaggerMyCoinComponent;
import com.tysq.ty_android.feature.coin.myCoin.di.MyCoinModule;
import com.tysq.ty_android.base.SimpleLoadMoreFragment;
import com.tysq.ty_android.feature.web.TyWebViewActivity;
import com.tysq.ty_android.utils.TyUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.Override;

import eventbus.MyCoinRefreshEvent;
import response.coin.MyCoinDetailResp;
import response.coin.MyCoinResp;

/**
 * author       : frog
 * time         : 2019-07-18 17:17
 * desc         : 我的金币
 * version      : 1.3.0
 */

public final class MyCoinFragment
        extends SimpleLoadMoreStrengthenFragment<MyCoinPresenter, MyCoinDetailResp.DetailsInfoBean>
        implements MyCoinView {

    public static final String TAG = "MyCoinFragment";

    private final MyCoinResp myCoinResp = new MyCoinResp("0", 0);

    public static MyCoinFragment newInstance() {

        Bundle args = new Bundle();

        MyCoinFragment fragment = new MyCoinFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new MyCoinAdapter(getContext(), myCoinResp, mData);
    }

    @Override
    protected void registerDagger() {
        DaggerMyCoinComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .myCoinModule(new MyCoinModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.getCoin(start, pageSize, isFirst);
    }

    @Override
    public void onGetCoinFailure(boolean isFirst) {
        onHandleError(isFirst);
    }

    @Override
    public void onGetCoin(MyCoinDetailResp value, boolean isFirst) {
        myCoinResp.setCount(TyUtils.formatDotNum(value.getTotalNumber()));
        myCoinResp.setCountNum(value.getTotalNumber());

        onHandleResponseData(value.getDetailsInfo(), isFirst);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(MyCoinRefreshEvent event){
        getFirstData(LoadType.CUSTOM);
    }

    @Override
    public int getTitleId() {
        return R.string.my_coin;
    }

    @Override
    public int getConfirmId() {
        return R.string.my_coin_des;
    }

    @Override
    public void setConfirmClick() {
        TyWebViewActivity.startActivity(getContext(),
                        Constant.HtmlAPI.COIN);
    }

    @Override
    public int getConfirmBackground() {
        return 0;
    }

    @Override
    public int getConfirmTextColor() {
        return R.color.main_text_color;
    }


}
