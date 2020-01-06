package com.tysq.ty_android.feature.review.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.review.ReviewActivity;
import dagger.Component;

@ActivityScope
@Component(
    modules = {ReviewModule.class},
    dependencies = AppComponent.class
)
public interface ReviewComponent {
  void inject(ReviewActivity reviewActivity);
}
