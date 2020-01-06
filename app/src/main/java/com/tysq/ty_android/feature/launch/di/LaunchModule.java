package com.tysq.ty_android.feature.launch.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.launch.LaunchView;
import dagger.Module;
import dagger.Provides;

@Module
public final class LaunchModule {
  private final LaunchView mView;

  public LaunchModule(LaunchView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public LaunchView getLaunchView() {
    return this.mView;
  }
}
