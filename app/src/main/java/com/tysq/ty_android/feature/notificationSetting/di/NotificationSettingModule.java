package com.tysq.ty_android.feature.notificationSetting.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.notificationSetting.NotificationSettingView;
import dagger.Module;
import dagger.Provides;

@Module
public final class NotificationSettingModule {
  private final NotificationSettingView mView;

  public NotificationSettingModule(NotificationSettingView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public NotificationSettingView getNotificationSettingView() {
    return this.mView;
  }
}
