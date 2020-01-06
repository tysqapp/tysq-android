package com.tysq.ty_android.feature.notificationSetting;

import com.bit.view.IView;

import response.notification.NotificationConfigResp;

public interface NotificationSettingView extends IView {

    public void getNotificationConfig(NotificationConfigResp configResp);

    public void putNotificationConfigError();

    public void putNotificationConfig();
}
