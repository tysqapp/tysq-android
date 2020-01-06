package com.tysq.ty_android.feature.coin.orderCoin;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.coin.CoinOrderResp;

public final class OrderCoinPresenter extends BasePresenter<OrderCoinView> {
    @Inject
    public OrderCoinPresenter(OrderCoinView view) {
      super(view);
    }

    public void getOrderCoin(int status, int start, int size, boolean isFirst){
        RetrofitFactory
                .getApiService()
                .getCoinOrder(status, start, size)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<CoinOrderResp>(mySelf) {
                  @Override
                  protected void onError(int code, String message) {
                      showErrorMsg(code, message);
                      mView.onGetOrderCoinFailure(isFirst);
                  }

                  @Override
                  protected void onSuccessRes(@NonNull CoinOrderResp value) {
                      mView.onGetOrderCoin(value, isFirst);
                  }
                });

    }
}
