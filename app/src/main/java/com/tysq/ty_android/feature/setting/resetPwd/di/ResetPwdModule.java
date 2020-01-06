package com.tysq.ty_android.feature.setting.resetPwd.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.setting.resetPwd.ResetPwdView;
import dagger.Module;
import dagger.Provides;

@Module
public final class ResetPwdModule {
  private final ResetPwdView mView;

  public ResetPwdModule(ResetPwdView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public ResetPwdView getResetPwdView() {
    return this.mView;
  }
}
