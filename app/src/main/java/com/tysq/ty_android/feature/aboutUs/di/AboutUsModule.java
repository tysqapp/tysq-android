package com.tysq.ty_android.feature.aboutUs.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.aboutUs.AboutUsView;
import dagger.Module;
import dagger.Provides;

@Module
public final class AboutUsModule {
  private final AboutUsView mView;

  public AboutUsModule(AboutUsView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public AboutUsView getAboutUsView() {
    return this.mView;
  }
}
