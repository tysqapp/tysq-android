package com.tysq.ty_android.feature.login.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.login.LoginView;
import dagger.Module;
import dagger.Provides;

@Module
public final class LoginModule {
  private final LoginView mView;

  public LoginModule(LoginView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public LoginView getLoginView() {
    return this.mView;
  }
}
