package com.tysq.ty_android.feature.articleCollect.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.articleCollect.ArticleCollectView;

import dagger.Module;
import dagger.Provides;

@Module
public final class ArticleCollectModule {
    private final ArticleCollectView mView;

    public ArticleCollectModule(ArticleCollectView view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ArticleCollectView getArticleCollectView() {
        return this.mView;
    }
}
