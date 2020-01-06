package com.tysq.ty_android.feature.rank.orderRank;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.rank.RankOrderResp;

public final class OrderRankPresenter extends BasePresenter<OrderRankView> {
    @Inject
    public OrderRankPresenter(OrderRankView view) {
        super(view);
    }

    public void getScoreOrder(int start, int pageSize, boolean isFirst) {
        RetrofitFactory
                .getApiService()
                .getScoreOrder(start, pageSize)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<RankOrderResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onGetScoreOrderFailure(isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull RankOrderResp value) {
                        mView.onGetScoreOrder(value,isFirst);
                    }
                });
    }
}
