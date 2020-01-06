package com.tysq.ty_android.feature.mine.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.mine.MineFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {MineModule.class},
    dependencies = AppComponent.class
)
public interface MineComponent {
  void inject(MineFragment mineFragment);
}
