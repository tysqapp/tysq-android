package com.tysq.ty_android.feature.coin.coinWithdrawLog.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.coin.coinWithdrawLog.CoinWithdrawLogFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {CoinWithdrawLogModule.class},
    dependencies = AppComponent.class
)
public interface CoinWithdrawLogComponent {
  void inject(CoinWithdrawLogFragment coinWithdrawLogFragment);
}
