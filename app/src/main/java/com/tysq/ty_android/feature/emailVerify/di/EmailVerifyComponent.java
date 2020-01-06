package com.tysq.ty_android.feature.emailVerify.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.emailVerify.EmailVerifyActivity;
import dagger.Component;

@ActivityScope
@Component(
    modules = {EmailVerifyModule.class},
    dependencies = AppComponent.class
)
public interface EmailVerifyComponent {
  void inject(EmailVerifyActivity emailVerifyActivity);
}
