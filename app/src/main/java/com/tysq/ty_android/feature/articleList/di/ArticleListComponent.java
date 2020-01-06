package com.tysq.ty_android.feature.articleList.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.articleList.ArticleListFragment;

import dagger.Component;

@ActivityScope
@Component(
    modules = {ArticleListModule.class},
    dependencies = AppComponent.class
)
public interface ArticleListComponent {
  void inject(ArticleListFragment articleListFragment);
}
