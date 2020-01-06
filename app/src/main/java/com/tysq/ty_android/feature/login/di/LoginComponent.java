package com.tysq.ty_android.feature.login.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.login.LoginActivity;
import dagger.Component;

@ActivityScope
@Component(
    modules = {LoginModule.class},
    dependencies = AppComponent.class
)
public interface LoginComponent {
  void inject(LoginActivity loginActivity);
}
