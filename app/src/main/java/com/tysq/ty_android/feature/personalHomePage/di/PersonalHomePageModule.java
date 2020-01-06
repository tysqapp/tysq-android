package com.tysq.ty_android.feature.personalHomePage.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.personalHomePage.PersonalHomePageView;
import dagger.Module;
import dagger.Provides;

@Module
public final class PersonalHomePageModule {
  private final PersonalHomePageView mView;

  public PersonalHomePageModule(PersonalHomePageView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public PersonalHomePageView getPersonalHomePageView() {
    return this.mView;
  }
}
