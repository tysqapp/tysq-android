package com.tysq.ty_android.feature.cloud.cloudList.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.cloud.cloudList.CloudListFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {CloudListModule.class},
    dependencies = AppComponent.class
)
public interface CloudListComponent {
  void inject(CloudListFragment cloudListFragment);
}
