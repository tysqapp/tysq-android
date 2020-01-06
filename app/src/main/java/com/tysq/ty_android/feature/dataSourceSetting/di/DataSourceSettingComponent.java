package com.tysq.ty_android.feature.dataSourceSetting.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.dataSourceSetting.DataSourceSettingFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {DataSourceSettingModule.class},
    dependencies = AppComponent.class
)
public interface DataSourceSettingComponent {
  void inject(DataSourceSettingFragment dataSourceSettingFragment);
}
