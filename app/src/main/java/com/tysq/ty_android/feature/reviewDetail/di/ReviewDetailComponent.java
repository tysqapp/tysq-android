package com.tysq.ty_android.feature.reviewDetail.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.reviewDetail.fragment.ReviewDetailFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {ReviewDetailModule.class},
    dependencies = AppComponent.class
)
public interface ReviewDetailComponent {
  void inject(ReviewDetailFragment reviewDetailFragment);
}
