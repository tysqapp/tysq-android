package com.tysq.ty_android.feature.reviewDetail.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.reviewDetail.fragment.ReviewDetailView;

import dagger.Module;
import dagger.Provides;

@Module
public final class ReviewDetailModule {
    private final ReviewDetailView mView;

    public ReviewDetailModule(ReviewDetailView view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ReviewDetailView getReviewDetailView() {
        return this.mView;
    }
}
