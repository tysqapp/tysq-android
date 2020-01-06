package com.tysq.ty_android.feature.reviewDetail.activity;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.rank.JudgementResp;

public final class ReviewDetailPresenter extends BasePresenter<ReviewDetailView> {
    @Inject
    public ReviewDetailPresenter(ReviewDetailView view) {
        super(view);
    }

    public void getJudgement(String articleId) {
        RetrofitFactory
                .getApiService()
                .getJudgement(Constant.JudgementType.COMMENT, articleId, articleId)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<JudgementResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull JudgementResp value) {
                        mView.hideDialog();
                        mView.onGetJudgement(value);
                    }
                });
    }
}
