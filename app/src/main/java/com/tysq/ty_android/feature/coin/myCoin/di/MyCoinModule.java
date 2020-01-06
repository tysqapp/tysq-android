package com.tysq.ty_android.feature.coin.myCoin.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.coin.myCoin.MyCoinView;
import dagger.Module;
import dagger.Provides;

@Module
public final class MyCoinModule {
  private final MyCoinView mView;

  public MyCoinModule(MyCoinView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public MyCoinView getMyCoinView() {
    return this.mView;
  }
}
