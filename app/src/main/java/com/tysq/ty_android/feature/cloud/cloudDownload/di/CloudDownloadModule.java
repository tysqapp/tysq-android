package com.tysq.ty_android.feature.cloud.cloudDownload.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.cloud.cloudDownload.CloudDownloadView;
import dagger.Module;
import dagger.Provides;

@Module
public final class CloudDownloadModule {
  private final CloudDownloadView mView;

  public CloudDownloadModule(CloudDownloadView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public CloudDownloadView getCloudDownloadView() {
    return this.mView;
  }
}
