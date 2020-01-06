package com.tysq.ty_android.feature.register;

import com.bit.view.IView;

import response.login.RegisterResp;
import response.login.RespCaptcha;

public interface RegisterView extends IView {

    void onRegisterFailure();

    void onLoginSuc(RegisterResp registerResp);

    void onLoginFailure(String email);

    void onGetVerifyCodeError();

    void onGetVerifyCode(RespCaptcha value);
}
