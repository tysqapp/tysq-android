package com.tysq.ty_android.feature.articleReport.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.articleReport.ArticleReportFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {ArticleReportModule.class},
    dependencies = AppComponent.class
)
public interface ArticleReportComponent {
  void inject(ArticleReportFragment articleReportFragment);
}
