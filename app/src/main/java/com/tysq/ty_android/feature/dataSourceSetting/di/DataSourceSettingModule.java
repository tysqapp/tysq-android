package com.tysq.ty_android.feature.dataSourceSetting.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.dataSourceSetting.DataSourceSettingView;
import dagger.Module;
import dagger.Provides;

@Module
public final class DataSourceSettingModule {
  private final DataSourceSettingView mView;

  public DataSourceSettingModule(DataSourceSettingView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public DataSourceSettingView getDataSourceSettingView() {
    return this.mView;
  }
}
