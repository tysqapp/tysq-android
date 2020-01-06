package com.tysq.ty_android.feature.launch.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.launch.LaunchActivity;
import dagger.Component;

@ActivityScope
@Component(
    modules = {LaunchModule.class},
    dependencies = AppComponent.class
)
public interface LaunchComponent {
  void inject(LaunchActivity launchActivity);
}
