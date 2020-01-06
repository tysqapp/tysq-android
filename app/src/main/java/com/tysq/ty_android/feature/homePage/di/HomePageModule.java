package com.tysq.ty_android.feature.homePage.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.homePage.HomePageView;
import dagger.Module;
import dagger.Provides;

@Module
public final class HomePageModule {
  private final HomePageView mView;

  public HomePageModule(HomePageView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public HomePageView getHomePageView() {
    return this.mView;
  }
}
