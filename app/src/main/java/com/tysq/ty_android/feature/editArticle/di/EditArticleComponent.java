package com.tysq.ty_android.feature.editArticle.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.editArticle.EditArticleActivity;
import dagger.Component;

@ActivityScope
@Component(
    modules = {EditArticleModule.class},
    dependencies = AppComponent.class
)
public interface EditArticleComponent {
  void inject(EditArticleActivity editArticleActivity);
}
