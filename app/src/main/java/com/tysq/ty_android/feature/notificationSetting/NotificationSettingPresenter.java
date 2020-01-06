package com.tysq.ty_android.feature.notificationSetting;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import request.NotificationConfigReq;
import response.notification.NotificationConfigResp;

public final class NotificationSettingPresenter extends BasePresenter<NotificationSettingView> {
    @Inject
    public NotificationSettingPresenter(NotificationSettingView view) {
      super(view);
    }

    //获取通知设置的数据
    public void getNotificationConfig(){

      RetrofitFactory
              .getApiService()
              .getNotificationConfig()
              .compose(RxParser.handleSingleDataResult())
              .subscribe(new RxSingleSubscriber<NotificationConfigResp>(mySelf) {
                  @Override
                  protected void onError(int code, String message) {
                      showErrorMsg(code, message);
                  }

                  @Override
                  protected void onSuccessRes(@NonNull NotificationConfigResp value) {
                      mView.hideDialog();
                      mView.getNotificationConfig(value);
                  }
              });
    }

    //提交通知设置的数据
    public void putNotificationConfig(NotificationConfigReq configReq){
        RetrofitFactory
                .getApiService()
                .putNotificationConfig(configReq)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {

                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.putNotificationConfigError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.putNotificationConfig();
                    }
                });
    }

}
