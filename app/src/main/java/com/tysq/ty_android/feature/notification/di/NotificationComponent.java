package com.tysq.ty_android.feature.notification.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.notification.NotificationFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {NotificationModule.class},
    dependencies = AppComponent.class
)
public interface NotificationComponent {
  void inject(NotificationFragment notificationFragment);
}
