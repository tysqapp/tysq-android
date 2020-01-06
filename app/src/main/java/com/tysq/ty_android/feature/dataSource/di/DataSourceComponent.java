package com.tysq.ty_android.feature.dataSource.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.dataSource.DataSourceActivity;
import dagger.Component;

@ActivityScope
@Component(
    modules = {DataSourceModule.class},
    dependencies = AppComponent.class
)
public interface DataSourceComponent {
  void inject(DataSourceActivity dataSourceActivity);
}
