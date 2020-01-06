package com.tysq.ty_android.feature.coin.orderCoin.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.coin.orderCoin.OrderCoinFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {OrderCoinModule.class},
    dependencies = AppComponent.class
)
public interface OrderCoinComponent {
  void inject(OrderCoinFragment orderCoinFragment);
}
