package com.tysq.ty_android.feature.cloud.cloudList.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.cloud.cloudList.CloudListView;
import dagger.Module;
import dagger.Provides;

@Module
public final class CloudListModule {
  private final CloudListView mView;

  public CloudListModule(CloudListView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public CloudListView getCloudListView() {
    return this.mView;
  }
}
