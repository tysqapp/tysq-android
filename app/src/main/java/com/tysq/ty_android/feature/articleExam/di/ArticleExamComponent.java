package com.tysq.ty_android.feature.articleExam.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.articleExam.ArticleExamFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {ArticleExamModule.class},
    dependencies = AppComponent.class
)
public interface ArticleExamComponent {
  void inject(ArticleExamFragment articleExamFragment);
}
