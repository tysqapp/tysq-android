package com.tysq.ty_android.feature.articleDetail.activity.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import dagger.Component;

@ActivityScope
@Component(
    modules = {ArticleDetailModule.class},
    dependencies = AppComponent.class
)
public interface ArticleDetailComponent {
  void inject(ArticleDetailActivity articleDetailActivity);
}
