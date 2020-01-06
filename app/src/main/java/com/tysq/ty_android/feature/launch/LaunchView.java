package com.tysq.ty_android.feature.launch;

import com.bit.view.IView;

import response.UpdateResp;

public interface LaunchView extends IView {

    void onGetUpdateInfo(UpdateResp value);

    void onGetUpdateInfoError();
}
