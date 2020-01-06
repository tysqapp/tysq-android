package com.tysq.ty_android.feature.notification;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import request.NotificationReadReq;
import response.notification.NotificationReadedResp;
import response.notification.NotificationResp;
import response.notification.NotifyInfoResp;
import response.notification.NotifyUnReadResp;

public final class NotificationPresenter extends BasePresenter<NotificationView> {
    @Inject
    public NotificationPresenter(NotificationView view) {
        super(view);
    }

    public void loadNotification(int start,
                                 int pageSize,
                                 boolean isFirst){

        RetrofitFactory
                .getApiService()
                .getNotificationList(pageSize, start)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<NotifyInfoResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull NotifyInfoResp value) {
                        mView.hideDialog();
                        if (value != null || value.getNotificationResps().size() > 0){
                            mView.onLoadNotification(value, value.getTotalNumber(), isFirst);
                        }
                    }
                });
    }

    public void putNotificationRead(String notifyId){
        RetrofitFactory
                .getApiService()
                .putNotificationRead(new NotificationReadReq(notifyId))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onPutNotificationRead();
                    }
                });
    }

    /**
     * 设置通知全部为已读
     */
    public void putNotificationAllRead(){
        RetrofitFactory
                .getApiService()
                .putNotificationAllRead()
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<NotificationReadedResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull NotificationReadedResp value) {
                        mView.onPutNotificationAllRead(value);
                    }
                });
    }

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
                        mView.onGetNotifyUnCountRead(value.getUnReadCount());
                    }
                });
    }
}
