package com.tysq.ty_android.feature.myFans.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.myFans.MyFansView;
import dagger.Module;
import dagger.Provides;

@Module
public final class MyFansModule {
  private final MyFansView mView;

  public MyFansModule(MyFansView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public MyFansView getMyFansView() {
    return this.mView;
  }
}
