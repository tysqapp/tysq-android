package com.tysq.ty_android.feature.reportDetail.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.reportDetail.ReportDetailView;
import dagger.Module;
import dagger.Provides;

@Module
public final class ReportDetailModule {
  private final ReportDetailView mView;

  public ReportDetailModule(ReportDetailView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public ReportDetailView getReportDetailView() {
    return this.mView;
  }
}
