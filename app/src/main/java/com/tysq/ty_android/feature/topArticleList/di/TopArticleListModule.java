package com.tysq.ty_android.feature.topArticleList.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.topArticleList.TopArticleListView;
import dagger.Module;
import dagger.Provides;

@Module
public final class TopArticleListModule {
  private final TopArticleListView mView;

  public TopArticleListModule(TopArticleListView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public TopArticleListView getTopArticleListView() {
    return this.mView;
  }
}
