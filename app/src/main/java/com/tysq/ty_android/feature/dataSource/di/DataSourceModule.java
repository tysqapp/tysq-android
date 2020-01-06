package com.tysq.ty_android.feature.dataSource.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.dataSource.DataSourceView;
import dagger.Module;
import dagger.Provides;

@Module
public final class DataSourceModule {
  private final DataSourceView mView;

  public DataSourceModule(DataSourceView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public DataSourceView getDataSourceView() {
    return this.mView;
  }
}
