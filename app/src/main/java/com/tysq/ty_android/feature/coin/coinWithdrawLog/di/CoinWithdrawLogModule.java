package com.tysq.ty_android.feature.coin.coinWithdrawLog.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.coin.coinWithdrawLog.CoinWithdrawLogView;
import dagger.Module;
import dagger.Provides;

@Module
public final class CoinWithdrawLogModule {
  private final CoinWithdrawLogView mView;

  public CoinWithdrawLogModule(CoinWithdrawLogView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public CoinWithdrawLogView getCoinWithdrawLogView() {
    return this.mView;
  }
}
