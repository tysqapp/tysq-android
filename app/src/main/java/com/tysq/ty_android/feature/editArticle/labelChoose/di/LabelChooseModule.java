package com.tysq.ty_android.feature.editArticle.labelChoose.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.editArticle.labelChoose.LabelChooseView;
import dagger.Module;
import dagger.Provides;

@Module
public final class LabelChooseModule {
  private final LabelChooseView mView;

  public LabelChooseModule(LabelChooseView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public LabelChooseView getLabelChooseView() {
    return this.mView;
  }
}
