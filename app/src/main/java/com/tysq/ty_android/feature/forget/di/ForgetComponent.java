package com.tysq.ty_android.feature.forget.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.forget.ForgetActivity;
import dagger.Component;

@ActivityScope
@Component(
    modules = {ForgetModule.class},
    dependencies = AppComponent.class
)
public interface ForgetComponent {
  void inject(ForgetActivity forgetActivity);
}
