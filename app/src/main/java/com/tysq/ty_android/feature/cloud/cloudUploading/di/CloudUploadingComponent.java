package com.tysq.ty_android.feature.cloud.cloudUploading.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.cloud.cloudUploading.CloudUploadingFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {CloudUploadingModule.class},
    dependencies = AppComponent.class
)
public interface CloudUploadingComponent {
  void inject(CloudUploadingFragment cloudUploadingFragment);
}
