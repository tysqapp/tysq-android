package com.tysq.ty_android.feature.person.personInfoChange.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.person.personInfoChange.PersonInfoChangeFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {PersonInfoChangeModule.class},
    dependencies = AppComponent.class
)
public interface PersonInfoChangeComponent {
  void inject(PersonInfoChangeFragment personInfoChangeFragment);
}
