package com.tysq.ty_android.feature.coin.myCoin;

import com.bit.view.IView;

import response.coin.MyCoinDetailResp;

public interface MyCoinView extends IView {
    void onGetCoinFailure(boolean isFirst);

    void onGetCoin(MyCoinDetailResp value, boolean isFirst);
}
