package com.tysq.ty_android.feature.articleList.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.articleList.ArticleListView;
import dagger.Module;
import dagger.Provides;

@Module
public final class ArticleListModule {
  private final ArticleListView mView;

  public ArticleListModule(ArticleListView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public ArticleListView getArticleListView() {
    return this.mView;
  }
}
