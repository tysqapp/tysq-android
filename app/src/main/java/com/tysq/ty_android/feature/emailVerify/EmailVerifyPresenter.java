package com.tysq.ty_android.feature.emailVerify;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import cache.User;
import request.EmailVerifyReq;
import response.common.EmailCodeResp;

public final class EmailVerifyPresenter extends BasePresenter<EmailVerifyView> {
    @Inject
    public EmailVerifyPresenter(EmailVerifyView view) {
        super(view);
    }

    /**
     * 向邮箱发送验证码
     */
    public void sendEmailCode(String email) {
        RetrofitFactory
                .getApiService()
                .getCaptcha(email, Constant.EmailCaptchaType.BIND_EMAIL)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<EmailCodeResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onSendEmailCodeFailure();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull EmailCodeResp value) {
                        mView.onSendEmailCode(value.getCaptchaId());
                    }
                });
    }

    /**
     * 验证邮箱
     *
     * @param email  邮箱
     * @param code   验证码
     * @param codeId 验证码id
     */
    public void verifyEmail(final String email,
                            String code,
                            String codeId) {
        EmailVerifyReq req = new EmailVerifyReq(email, code, codeId);
        RetrofitFactory
                .getApiService()
                .putEmailVerify(req)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object o) {
                        User user = UserCache.getDefault();
                        user.setEmailStatus(Constant.EmailValidateStatus.EMAIL_VERIFY_SUC);
                        UserCache.save(user);
                        mView.onVerifyEmail();
                    }
                });
    }
}
