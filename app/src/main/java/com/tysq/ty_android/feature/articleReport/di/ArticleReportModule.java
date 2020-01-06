package com.tysq.ty_android.feature.articleReport.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.articleReport.ArticleReportView;
import dagger.Module;
import dagger.Provides;

@Module
public final class ArticleReportModule {
  private final ArticleReportView mView;

  public ArticleReportModule(ArticleReportView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public ArticleReportView getArticleReportView() {
    return this.mView;
  }
}
