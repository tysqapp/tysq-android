package com.tysq.ty_android.feature.forbidReview.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.forbidReview.ForbidReviewView;
import dagger.Module;
import dagger.Provides;

@Module
public final class ForbidReviewModule {
  private final ForbidReviewView mView;

  public ForbidReviewModule(ForbidReviewView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public ForbidReviewView getBannerView() {
    return this.mView;
  }
}
