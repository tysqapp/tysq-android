package com.tysq.ty_android.feature.myReview.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.myReview.MyReviewView;
import dagger.Module;
import dagger.Provides;

@Module
public final class MyReviewModule {
  private final MyReviewView mView;

  public MyReviewModule(MyReviewView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public MyReviewView getMyReviewView() {
    return this.mView;
  }
}
