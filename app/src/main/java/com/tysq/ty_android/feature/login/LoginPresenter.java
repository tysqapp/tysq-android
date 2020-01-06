package com.tysq.ty_android.feature.login;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxObservableSubscriber;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import request.LoginReq;
import response.login.LoginResp;
import response.login.RespCaptcha;

public final class LoginPresenter extends BasePresenter<LoginView> {

    @Inject
    public LoginPresenter(LoginView view) {
        super(view);
    }

    public void loadCode() {
        RetrofitFactory
                .getApiService()
                .getVerifyCode(Constant.CaptchaType.LOGIN)
                .compose(RxParser.<RespCaptcha>handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<RespCaptcha>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadFail();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull RespCaptcha value) {
                        mView.onLoad(value.getCaptchaId(), value.getCaptchaBase64());
                    }
                });

    }

    public void login(String email, String pwd, String code, String codeId) {
        LoginReq req = new LoginReq(email, pwd, code, codeId);

        RetrofitFactory
                .getApiService()
                .postLogin(req)
                .compose(RxParser.handleObservableDataResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<LoginResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoginFailure(code, message);
                    }

                    @Override
                    protected void onSuccessRes(LoginResp value) {
                        // 保存用户信息
                        UserCache.save(value.getAccountInfo());
                        mView.onLogin();
                    }
                });
    }
}
