package com.tysq.ty_android.feature.homePage.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.homePage.HomePageFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {HomePageModule.class},
    dependencies = AppComponent.class
)
public interface HomePageComponent {
  void inject(HomePageFragment homePageFragment);
}
