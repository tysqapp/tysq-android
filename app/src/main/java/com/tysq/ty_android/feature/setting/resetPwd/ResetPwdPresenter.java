package com.tysq.ty_android.feature.setting.resetPwd;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import request.ChangePwdReq;

public final class ResetPwdPresenter extends BasePresenter<ResetPwdView> {
    @Inject
    public ResetPwdPresenter(ResetPwdView view) {
        super(view);
    }

    public void resetPwd(String oldPwd, String newPwd) {
        ChangePwdReq req = new ChangePwdReq(oldPwd, newPwd);

        RetrofitFactory
                .getApiService()
                .putChangePwd(req)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onResetPwd();
                    }
                });
    }
}
