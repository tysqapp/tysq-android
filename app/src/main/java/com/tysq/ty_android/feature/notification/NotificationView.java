package com.tysq.ty_android.feature.notification;

import com.bit.view.IView;

import java.util.List;

import response.notification.NotificationReadedResp;
import response.notification.NotificationResp;
import response.notification.NotifyInfoResp;

public interface NotificationView extends IView {
    void onLoadNotification(NotifyInfoResp data, int totalSize, boolean isFirst);

    void onPutNotificationRead();

    void onPutNotificationAllRead(NotificationReadedResp value);

    void onGetNotifyUnCountRead(int count);
}
