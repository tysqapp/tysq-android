package com.tysq.ty_android.feature.register;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxObservableSubscriber;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import request.LoginReq;
import request.RegisterReq;
import response.login.LoginResp;
import response.login.RegisterResp;
import response.login.RespCaptcha;

public final class RegisterPresenter extends BasePresenter<RegisterView> {

    @Inject
    public RegisterPresenter(RegisterView view) {
        super(view);
    }

    /**
     * 注册
     *
     * @param email        邮箱
     * @param pwd          密码
     * @param referralCode 推荐码
     */
    public void register(final String email,
                         final String pwd,
                         final String referralCode,
                         final String verifyCodeId,
                         final String verifyCode) {
        RegisterReq req = new RegisterReq(email, pwd, referralCode,verifyCodeId,verifyCode);

        RetrofitFactory
                .getApiService()
                .postRegister(req)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<RegisterResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onRegisterFailure();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull RegisterResp value) {
                        login(value, email, pwd);
                    }
                });
    }

    /**
     * 注册成功后进行登录
     *
     * @param email 邮箱
     * @param pwd   密码
     */
    public void login(RegisterResp registerResp, String email, String pwd) {
        LoginReq req = new LoginReq(email, pwd, "", "");
        RetrofitFactory
                .getApiService()
                .postLogin(req)
                .compose(RxParser.handleObservableDataResult())
                .subscribe(new RxObservableSubscriber<LoginResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoginFailure(email);
                    }

                    @Override
                    protected void onSuccessRes(LoginResp value) {
                        UserCache.save(value.getAccountInfo());
                        mView.onLoginSuc(registerResp);
                    }
                });
    }

    public void getVerifyCode() {

        RetrofitFactory
                .getApiService()
                .getVerifyCode(Constant.CaptchaType.REGISTER)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<RespCaptcha>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onGetVerifyCodeError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull RespCaptcha value) {
                        mView.onGetVerifyCode(value);
                    }
                });

    }
}
