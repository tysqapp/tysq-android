package com.tysq.ty_android.feature.forbidList.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.forbidList.ForbidListFragment;

import dagger.Component;

@ActivityScope
@Component(
    modules = {ForbidListModule.class},
    dependencies = AppComponent.class
)
public interface ForbidListComponent {
  void inject(ForbidListFragment forbidListFragment);
}
