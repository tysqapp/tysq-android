package com.tysq.ty_android.feature.editArticle.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.editArticle.EditArticleView;
import dagger.Module;
import dagger.Provides;

@Module
public final class EditArticleModule {
  private final EditArticleView mView;

  public EditArticleModule(EditArticleView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public EditArticleView getEditArticleView() {
    return this.mView;
  }
}
