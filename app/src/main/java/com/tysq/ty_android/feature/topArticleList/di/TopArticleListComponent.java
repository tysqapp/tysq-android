package com.tysq.ty_android.feature.topArticleList.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.topArticleList.TopArticleListFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {TopArticleListModule.class},
    dependencies = AppComponent.class
)
public interface TopArticleListComponent {
  void inject(TopArticleListFragment topArticleListFragment);
}
