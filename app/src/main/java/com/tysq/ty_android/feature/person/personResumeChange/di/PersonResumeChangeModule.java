package com.tysq.ty_android.feature.person.personResumeChange.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.person.personResumeChange.PersonResumeChangeView;
import dagger.Module;
import dagger.Provides;

@Module
public final class PersonResumeChangeModule {
  private final PersonResumeChangeView mView;

  public PersonResumeChangeModule(PersonResumeChangeView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public PersonResumeChangeView getPersonResumeChangeView() {
    return this.mView;
  }
}
