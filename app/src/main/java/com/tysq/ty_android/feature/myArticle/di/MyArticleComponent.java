package com.tysq.ty_android.feature.myArticle.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.myArticle.MyArticleFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {MyArticleModule.class},
    dependencies = AppComponent.class
)
public interface MyArticleComponent {
  void inject(MyArticleFragment myArticleFragment);
}
