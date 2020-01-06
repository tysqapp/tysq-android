package com.tysq.ty_android.feature.articleCollect.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.articleCollect.ArticleCollectFragment;

import dagger.Component;

@ActivityScope
@Component(
        modules = {ArticleCollectModule.class},
        dependencies = AppComponent.class
)
public interface ArticleCollectComponent {
    void inject(ArticleCollectFragment articleCollectFragment);
}
