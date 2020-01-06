package com.tysq.ty_android.feature.aboutUs;

import com.bit.view.IView;

import response.UpdateResp;

public interface AboutUsView extends IView {
    void onGetUpdateInfoError();

    void onGetUpdateInfo(UpdateResp value);
}
