package com.tysq.ty_android.feature.cloudChoose.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.cloudChoose.CloudChooseFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {CloudChooseModule.class},
    dependencies = AppComponent.class
)
public interface CloudChooseComponent {
  void inject(CloudChooseFragment cloudChooseFragment);
}
