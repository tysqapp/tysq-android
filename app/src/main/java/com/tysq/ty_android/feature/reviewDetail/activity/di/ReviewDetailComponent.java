package com.tysq.ty_android.feature.reviewDetail.activity.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.reviewDetail.activity.ReviewDetailActivity;
import dagger.Component;

@ActivityScope
@Component(
    modules = {ReviewDetailModule.class},
    dependencies = AppComponent.class
)
public interface ReviewDetailComponent {
  void inject(ReviewDetailActivity reviewDetailActivity);
}
