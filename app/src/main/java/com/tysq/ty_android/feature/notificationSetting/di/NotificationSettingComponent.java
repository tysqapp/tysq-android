package com.tysq.ty_android.feature.notificationSetting.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.notificationSetting.NotificationSettingFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {NotificationSettingModule.class},
    dependencies = AppComponent.class
)
public interface NotificationSettingComponent {
  void inject(NotificationSettingFragment notificationSettingFragment);
}
