package com.tysq.ty_android.feature.articleExam.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.articleExam.ArticleExamView;
import dagger.Module;
import dagger.Provides;

@Module
public final class ArticleExamModule {
  private final ArticleExamView mView;

  public ArticleExamModule(ArticleExamView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public ArticleExamView getArticleExamView() {
    return this.mView;
  }
}
