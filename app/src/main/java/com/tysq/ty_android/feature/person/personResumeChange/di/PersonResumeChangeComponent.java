package com.tysq.ty_android.feature.person.personResumeChange.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.person.personResumeChange.PersonResumeChangeFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {PersonResumeChangeModule.class},
    dependencies = AppComponent.class
)
public interface PersonResumeChangeComponent {
  void inject(PersonResumeChangeFragment personResumeChangeFragment);
}
