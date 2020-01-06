package com.tysq.ty_android.feature.register.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.register.RegisterActivity;
import dagger.Component;

@ActivityScope
@Component(
    modules = {RegisterModule.class},
    dependencies = AppComponent.class
)
public interface RegisterComponent {
  void inject(RegisterActivity registerActivity);
}
