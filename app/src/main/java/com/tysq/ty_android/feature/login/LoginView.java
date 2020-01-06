package com.tysq.ty_android.feature.login;

import com.bit.view.IView;

public interface LoginView extends IView {

    void onLoad(String codeId, String base64);

    void onLoadFail();

    void onLoginFailure(int code, String message);

    void onLogin();
}
