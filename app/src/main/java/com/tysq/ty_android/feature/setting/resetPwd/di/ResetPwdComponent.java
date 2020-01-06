package com.tysq.ty_android.feature.setting.resetPwd.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.setting.resetPwd.ResetPwdFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {ResetPwdModule.class},
    dependencies = AppComponent.class
)
public interface ResetPwdComponent {
  void inject(ResetPwdFragment resetPwdFragment);
}
