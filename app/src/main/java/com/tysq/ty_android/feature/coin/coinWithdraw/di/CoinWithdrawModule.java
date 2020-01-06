package com.tysq.ty_android.feature.coin.coinWithdraw.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.coin.coinWithdraw.CoinWithdrawView;
import dagger.Module;
import dagger.Provides;

@Module
public final class CoinWithdrawModule {
  private final CoinWithdrawView mView;

  public CoinWithdrawModule(CoinWithdrawView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public CoinWithdrawView getCoinWithdrawView() {
    return this.mView;
  }
}
