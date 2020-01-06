package com.tysq.ty_android.feature.coin.orderCoin;

import com.bit.view.IView;

import response.coin.CoinOrderResp;

public interface OrderCoinView extends IView {
    void onGetOrderCoinFailure(boolean isFirst);

    void onGetOrderCoin(CoinOrderResp value, boolean isFirst);
}
