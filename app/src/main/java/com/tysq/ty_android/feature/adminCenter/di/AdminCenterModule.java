package com.tysq.ty_android.feature.adminCenter.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.adminCenter.AdminCenterView;
import dagger.Module;
import dagger.Provides;

@Module
public final class AdminCenterModule {
  private final AdminCenterView mView;

  public AdminCenterModule(AdminCenterView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public AdminCenterView getAdminCenterView() {
    return this.mView;
  }
}
