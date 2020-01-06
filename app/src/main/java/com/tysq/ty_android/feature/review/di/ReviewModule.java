package com.tysq.ty_android.feature.review.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.review.ReviewView;
import dagger.Module;
import dagger.Provides;

@Module
public final class ReviewModule {
  private final ReviewView mView;

  public ReviewModule(ReviewView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public ReviewView getReviewView() {
    return this.mView;
  }
}
