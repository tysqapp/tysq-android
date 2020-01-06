package com.tysq.ty_android.feature.rank.myRank.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.rank.myRank.MyRankView;
import dagger.Module;
import dagger.Provides;

@Module
public final class MyRankModule {
  private final MyRankView mView;

  public MyRankModule(MyRankView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public MyRankView getMyRankView() {
    return this.mView;
  }
}
