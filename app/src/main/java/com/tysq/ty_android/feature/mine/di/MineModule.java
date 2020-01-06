package com.tysq.ty_android.feature.mine.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.mine.MineView;
import dagger.Module;
import dagger.Provides;

@Module
public final class MineModule {
  private final MineView mView;

  public MineModule(MineView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public MineView getMineView() {
    return this.mView;
  }
}
