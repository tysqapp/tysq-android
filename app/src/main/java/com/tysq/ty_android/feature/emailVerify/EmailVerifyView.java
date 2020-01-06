package com.tysq.ty_android.feature.emailVerify;

import com.bit.view.IView;

public interface EmailVerifyView extends IView {

    void onSendEmailCodeFailure();

    void onSendEmailCode(String captchaId);

    void onVerifyEmail();
}
