package com.tysq.ty_android.feature.homePageSearch.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.homePageSearch.HomePageSearchView;
import dagger.Module;
import dagger.Provides;

@Module
public final class HomePageSearchModule {
  private final HomePageSearchView mView;

  public HomePageSearchModule(HomePageSearchView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public HomePageSearchView getHomePageSearchView() {
    return this.mView;
  }
}
