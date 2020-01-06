package com.tysq.ty_android.feature.myArticle.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.myArticle.MyArticleView;
import dagger.Module;
import dagger.Provides;

@Module
public final class MyArticleModule {
  private final MyArticleView mView;

  public MyArticleModule(MyArticleView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public MyArticleView getMyArticleView() {
    return this.mView;
  }
}
