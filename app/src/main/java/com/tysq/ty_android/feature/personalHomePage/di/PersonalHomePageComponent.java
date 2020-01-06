package com.tysq.ty_android.feature.personalHomePage.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.personalHomePage.PersonalHomePageFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {PersonalHomePageModule.class},
    dependencies = AppComponent.class
)
public interface PersonalHomePageComponent {
  void inject(PersonalHomePageFragment personalHomePageFragment);
}
