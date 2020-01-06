package com.tysq.ty_android.feature.forbidList.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.forbidList.ForbidListView;

import dagger.Module;
import dagger.Provides;

@Module
public final class ForbidListModule {
  private final ForbidListView mView;

  public ForbidListModule(ForbidListView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public ForbidListView getBannerListView() {
    return this.mView;
  }
}
