package com.tysq.ty_android.feature.aboutUs.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.aboutUs.AboutUsFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {AboutUsModule.class},
    dependencies = AppComponent.class
)
public interface AboutUsComponent {
  void inject(AboutUsFragment aboutUsFragment);
}
