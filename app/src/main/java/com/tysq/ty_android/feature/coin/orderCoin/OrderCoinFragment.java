package com.tysq.ty_android.feature.coin.orderCoin;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.SimpleLoadMoreStrengthenFragment;
import com.tysq.ty_android.feature.coin.orderCoin.di.DaggerOrderCoinComponent;
import com.tysq.ty_android.feature.coin.orderCoin.di.OrderCoinModule;
import java.lang.Override;

import response.coin.CoinOrderResp;
/**
 * author       : liaozhenlin
 * time         : 2019/11/5 9:40
 * desc         : 金币订单
 * version      : 1.5.0
 */
public final class OrderCoinFragment extends
        SimpleLoadMoreStrengthenFragment<OrderCoinPresenter, CoinOrderResp.CoinOrdersBean>
        implements OrderCoinView {

    public static OrderCoinFragment newInstance(){

        Bundle args = new Bundle();

        OrderCoinFragment fragment = new OrderCoinFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
      return new OrderCoinAdapter(getContext(), mData);

    }

    @Override
    protected void registerDagger() {
        DaggerOrderCoinComponent.builder()
        .appComponent(TyApplication.getAppComponent())
        .orderCoinModule(new OrderCoinModule(this))
        .build()
        .inject(this);
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.getOrderCoin(0, start, pageSize, isFirst);
    }

    @Override
    public int getTitleId() {
      return R.string.my_coin_order;
    }

    @Override
    public int getConfirmId() {
      return 0;
    }

    @Override
    public void setConfirmClick() {

    }

    @Override
    public int getConfirmBackground() {
      return 0;
    }

    @Override
    public int getConfirmTextColor() {
      return R.string.my_coin_order_des;
    }

    @Override
    public void onGetOrderCoinFailure(boolean isFirst) {
        onHandleError(isFirst);
    }

    @Override
    public void onGetOrderCoin(CoinOrderResp value, boolean isFirst) {
        onHandleResponseData(value.getCoinOrdersBeanList(), isFirst);
    }
}
