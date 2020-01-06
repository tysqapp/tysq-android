package com.tysq.ty_android.feature.coin.orderCoin.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.coin.orderCoin.OrderCoinView;
import dagger.Module;
import dagger.Provides;

@Module
public final class OrderCoinModule {
  private final OrderCoinView mView;

  public OrderCoinModule(OrderCoinView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public OrderCoinView getOrderCoinView() {
    return this.mView;
  }
}
