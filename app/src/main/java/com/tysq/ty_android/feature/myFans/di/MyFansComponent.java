package com.tysq.ty_android.feature.myFans.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.myFans.MyFansFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {MyFansModule.class},
    dependencies = AppComponent.class
)
public interface MyFansComponent {
  void inject(MyFansFragment myFansFragment);
}
