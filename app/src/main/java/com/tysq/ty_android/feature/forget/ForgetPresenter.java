package com.tysq.ty_android.feature.forget;

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
import request.ResetPwdReq;
import response.login.LoginResp;
import response.common.EmailCodeResp;

public final class ForgetPresenter extends BasePresenter<ForgetView> {
    @Inject
    public ForgetPresenter(ForgetView view) {
        super(view);
    }

    /**
     * 向邮箱发送验证码
     */
    public void sendEmailCode(String email) {
        RetrofitFactory
                .getApiService()
                .getCaptcha(email, Constant.EmailCaptchaType.RESET_PASSWORD)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<EmailCodeResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull EmailCodeResp value) {
                        mView.onSendEmailCode(value.getCaptchaId());
                    }
                });
    }

    /**
     * 验证 邮箱与验证码是否正确
     */
    public void checkEmailVerify(String email,
                                 String code,
                                 String codeId) {

        RetrofitFactory
                .getApiService()
                .getEmailVerify(email, code, codeId)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onCheckEmailVerifyError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onCheckEmailVerify();
                    }
                });

    }

    /**
     * 重置密码
     *
     * @param email  邮箱
     * @param pwd1   密码
     * @param pwd2   密码确认
     * @param code   验证码
     * @param codeId 验证码id
     */
    public void resetPwd(String email,
                         String pwd1,
                         String pwd2,
                         String code,
                         String codeId) {

        ResetPwdReq req = new ResetPwdReq(email, pwd1, pwd2, code, codeId);

        RetrofitFactory
                .getApiService()
                .putResetPwd(req)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int errCode, String message) {
                        showErrorMsg(errCode, message);
                        mView.onResetPwdFailure();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
//                        mView.onResetPwd();
                        login(email, pwd1);
                    }
                });

    }

    /**
     * 注册成功后进行登录
     *
     * @param email 邮箱
     * @param pwd   密码
     */
    public void login(String email, String pwd) {
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
                        mView.onLoginSuc();
                    }
                });
    }
}
