package com.tysq.ty_android.feature.rewardList.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.rewardList.RewardListFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {RewardListModule.class},
    dependencies = AppComponent.class
)
public interface RewardListComponent {
  void inject(RewardListFragment rewardListFragment);
}
