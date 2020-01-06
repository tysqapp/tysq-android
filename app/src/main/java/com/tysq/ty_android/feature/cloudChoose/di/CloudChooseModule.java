package com.tysq.ty_android.feature.cloudChoose.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.cloudChoose.CloudChooseView;
import dagger.Module;
import dagger.Provides;

@Module
public final class CloudChooseModule {
  private final CloudChooseView mView;

  public CloudChooseModule(CloudChooseView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public CloudChooseView getCloudChooseView() {
    return this.mView;
  }
}
