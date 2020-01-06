package com.tysq.ty_android.feature.articleDetail.activity.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailView;
import dagger.Module;
import dagger.Provides;

@Module
public final class ArticleDetailModule {
  private final ArticleDetailView mView;

  public ArticleDetailModule(ArticleDetailView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public ArticleDetailView getArticleDetailView() {
    return this.mView;
  }
}
