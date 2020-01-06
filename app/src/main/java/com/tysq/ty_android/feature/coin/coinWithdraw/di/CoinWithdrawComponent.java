package com.tysq.ty_android.feature.coin.coinWithdraw.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.coin.coinWithdraw.CoinWithdrawFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {CoinWithdrawModule.class},
    dependencies = AppComponent.class
)
public interface CoinWithdrawComponent {
  void inject(CoinWithdrawFragment coinWithdrawFragment);
}
