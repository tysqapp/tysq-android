package com.tysq.ty_android.feature.coin.myCoin;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.coin.MyCoinDetailResp;

public final class MyCoinPresenter extends BasePresenter<MyCoinView> {
    @Inject
    public MyCoinPresenter(MyCoinView view) {
        super(view);
    }

    public void getCoin(int start, int pageSize, boolean isFirst) {

        RetrofitFactory
                .getApiService()
                .getCoin(start,pageSize)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<MyCoinDetailResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onGetCoinFailure(isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull MyCoinDetailResp value) {
                        mView.onGetCoin(value,isFirst);
                    }
                });

    }
}
