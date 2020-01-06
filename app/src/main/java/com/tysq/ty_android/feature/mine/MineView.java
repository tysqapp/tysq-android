package com.tysq.ty_android.feature.mine;

import com.bit.view.IView;

import response.UserInfoResp;

public interface MineView extends IView {

    void onGetUserInfo();

    void onUploadInfo(UserInfoResp.Asset asset);

//    void onLoadConfig(int recRegisterSuccess);

    void getNotifyUnCountRead(int count);
}
