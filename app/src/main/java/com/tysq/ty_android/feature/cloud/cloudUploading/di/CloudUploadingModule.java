package com.tysq.ty_android.feature.cloud.cloudUploading.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.cloud.cloudUploading.CloudUploadingView;
import dagger.Module;
import dagger.Provides;

@Module
public final class CloudUploadingModule {
  private final CloudUploadingView mView;

  public CloudUploadingModule(CloudUploadingView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public CloudUploadingView getCloudUploadingView() {
    return this.mView;
  }
}
