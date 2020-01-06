package com.tysq.ty_android.feature.forget.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.forget.ForgetView;
import dagger.Module;
import dagger.Provides;

@Module
public final class ForgetModule {
  private final ForgetView mView;

  public ForgetModule(ForgetView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public ForgetView getForgetView() {
    return this.mView;
  }
}
