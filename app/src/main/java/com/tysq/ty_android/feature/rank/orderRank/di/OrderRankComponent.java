package com.tysq.ty_android.feature.rank.orderRank.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.rank.orderRank.OrderRankFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {OrderRankModule.class},
    dependencies = AppComponent.class
)
public interface OrderRankComponent {
  void inject(OrderRankFragment orderRankFragment);
}
