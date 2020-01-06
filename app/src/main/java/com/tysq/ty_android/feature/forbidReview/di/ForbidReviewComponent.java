package com.tysq.ty_android.feature.forbidReview.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.forbidReview.ForbidReviewFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {ForbidReviewModule.class},
    dependencies = AppComponent.class
)
public interface ForbidReviewComponent {
  void inject(ForbidReviewFragment forbidReviewFragment);
}
