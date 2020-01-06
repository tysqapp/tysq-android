package com.tysq.ty_android.feature.mine;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.UserInfoResp;
import response.notification.NotifyUnReadResp;

public final class MinePresenter extends BasePresenter<MineView> {
    @Inject
    public MinePresenter(MineView view) {
        super(view);
    }

    public void loadInfo() {
        loadUserInfo();
    }

    private void loadUserInfo() {
        RetrofitFactory
                .getApiService()
                .getUserInfo()
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<UserInfoResp>(mySelf, false) {
                    @Override
                    protected void onError(int code, String message) {

                    }

                    @Override
                    protected void onSuccessRes(@NonNull UserInfoResp value) {
                        UserCache.save(value.getAccountInfo());
                        mView.onGetUserInfo();
                        mView.onUploadInfo(value.getAsset());
                    }
                });
    }

//    public void loadConfig() {
//
//        ArrayList<String> list = new ArrayList<>();
//        list.add(Constant.Configuration.RECOMMEND_REGISTER_SUCCESS);
//
//        ConfigurationReq configurationReq = new ConfigurationReq();
//        configurationReq.setActions(list);
//
//        RetrofitFactory
//                .getApiService()
//                .getConfiguration(configurationReq)
//                .compose(RxParser.handleSingleDataResult())
//                .subscribe(new RxSingleSubscriber<ConfigurationResp>(mySelf, false) {
//                    @Override
//                    protected void onError(int code, String message) {
//                    }
//
//                    @Override
//                    protected void onSuccessRes(@NonNull ConfigurationResp value) {
//                        mView.onLoadConfig(value.getRecRegisterSuccess());
//                    }
//                });
//    }

    public void getNotifyUnReadCount() {
        RetrofitFactory
                .getApiService()
                .getNotifyUnReadCount()
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<NotifyUnReadResp>(mySelf, false) {
                    @Override
                    protected void onError(int code, String message) {
//                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull NotifyUnReadResp value) {
                        mView.getNotifyUnCountRead(value.getUnReadCount());
                    }
                });
    }
}
