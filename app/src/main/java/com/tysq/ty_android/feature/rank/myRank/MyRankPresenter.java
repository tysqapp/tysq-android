package com.tysq.ty_android.feature.rank.myRank;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.rank.RankDetailResp;

public final class MyRankPresenter extends BasePresenter<MyRankView> {
    @Inject
    public MyRankPresenter(MyRankView view) {
        super(view);
    }

    public void loadRankDetail(int totalSize,
                               int pageSize,
                               boolean isFirst) {

        RetrofitFactory
                .getApiService()
                .getScoreDetail(totalSize, pageSize)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<RankDetailResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadRankDetailFailure(isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull RankDetailResp value) {
                        mView.onLoadRankDetail(value, isFirst);
                    }
                });

    }
}
