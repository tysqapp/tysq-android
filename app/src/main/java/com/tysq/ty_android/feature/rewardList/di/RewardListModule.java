package com.tysq.ty_android.feature.rewardList.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.rewardList.RewardListView;
import dagger.Module;
import dagger.Provides;

@Module
public final class RewardListModule {
  private final RewardListView mView;

  public RewardListModule(RewardListView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public RewardListView getRewardListView() {
    return this.mView;
  }
}
