package com.tysq.ty_android.feature.rank.orderRank.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.rank.orderRank.OrderRankView;
import dagger.Module;
import dagger.Provides;

@Module
public final class OrderRankModule {
  private final OrderRankView mView;

  public OrderRankModule(OrderRankView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public OrderRankView getOrderRankView() {
    return this.mView;
  }
}
