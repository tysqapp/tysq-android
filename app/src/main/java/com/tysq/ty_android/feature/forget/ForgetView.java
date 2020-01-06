package com.tysq.ty_android.feature.forget;

import com.bit.view.IView;

public interface ForgetView extends IView {

    void onSendEmailCode(String captchaId);

    void onCheckEmailVerifyError();

    void onCheckEmailVerify();

    void onResetPwdFailure();

    void onLoginFailure(String email);

    void onLoginSuc();
}
