package com.tysq.ty_android.feature.person.personInfoChange.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.person.personInfoChange.PersonInfoChangeView;
import dagger.Module;
import dagger.Provides;

@Module
public final class PersonInfoChangeModule {
  private final PersonInfoChangeView mView;

  public PersonInfoChangeModule(PersonInfoChangeView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public PersonInfoChangeView getPersonInfoChangeView() {
    return this.mView;
  }
}
