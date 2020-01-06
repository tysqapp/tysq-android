package com.tysq.ty_android.feature.homePageSearch.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.homePageSearch.HomePageSearchFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {HomePageSearchModule.class},
    dependencies = AppComponent.class
)
public interface HomePageSearchComponent {
  void inject(HomePageSearchFragment homePageSearchFragment);
}
