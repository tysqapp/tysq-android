package com.tysq.ty_android.feature.register.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.register.RegisterView;
import dagger.Module;
import dagger.Provides;

@Module
public final class RegisterModule {
  private final RegisterView mView;

  public RegisterModule(RegisterView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public RegisterView getRegisterView() {
    return this.mView;
  }
}
