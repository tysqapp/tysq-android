package com.tysq.ty_android.feature.coin.myCoin.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.coin.myCoin.MyCoinFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {MyCoinModule.class},
    dependencies = AppComponent.class
)
public interface MyCoinComponent {
  void inject(MyCoinFragment myCoinFragment);
}
