package com.tysq.ty_android.feature.adminCenter.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.adminCenter.AdminCenterFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {AdminCenterModule.class},
    dependencies = AppComponent.class
)
public interface AdminCenterComponent {
  void inject(AdminCenterFragment adminCenterFragment);
}
