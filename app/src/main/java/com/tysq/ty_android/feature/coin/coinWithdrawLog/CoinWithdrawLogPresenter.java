package com.tysq.ty_android.feature.coin.coinWithdrawLog;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.coin.WithdrawLogResp;

public final class CoinWithdrawLogPresenter extends BasePresenter<CoinWithdrawLogView> {
    @Inject
    public CoinWithdrawLogPresenter(CoinWithdrawLogView view) {
        super(view);
    }

    public void getWithdrawLog(int start, int pageSize, boolean isFirst) {
        RetrofitFactory
                .getApiService()
                .getWithdrawLog(start, pageSize)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<WithdrawLogResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onGetWithdrawLogError(isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull WithdrawLogResp value) {
                        mView.onGetWithdrawLog(isFirst, value.getWithdrawReviewList());
                    }
                });
    }
}
