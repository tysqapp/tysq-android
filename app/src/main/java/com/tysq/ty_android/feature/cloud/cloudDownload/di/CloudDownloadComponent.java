package com.tysq.ty_android.feature.cloud.cloudDownload.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.cloud.cloudDownload.CloudDownloadFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {CloudDownloadModule.class},
    dependencies = AppComponent.class
)
public interface CloudDownloadComponent {
  void inject(CloudDownloadFragment cloudDownloadFragment);
}
