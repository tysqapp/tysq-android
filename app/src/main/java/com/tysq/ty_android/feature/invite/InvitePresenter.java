package com.tysq.ty_android.feature.invite;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.InviteResp;

public final class InvitePresenter extends BasePresenter<InviteView> {
    @Inject
    public InvitePresenter(InviteView view) {
        super(view);
    }

    public void getInvite(int start, int pageSize, boolean isFirst) {

        RetrofitFactory
                .getApiService()
                .getInvite(start, pageSize)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<InviteResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onGetInviteFailure(isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull InviteResp value) {
                        mView.onGetInvite(value, isFirst);
                    }
                });

    }
}
