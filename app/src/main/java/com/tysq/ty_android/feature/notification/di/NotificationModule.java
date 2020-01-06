package com.tysq.ty_android.feature.notification.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.notification.NotificationView;
import dagger.Module;
import dagger.Provides;

@Module
public final class NotificationModule {
  private final NotificationView mView;

  public NotificationModule(NotificationView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public NotificationView getNotificationView() {
    return this.mView;
  }
}
