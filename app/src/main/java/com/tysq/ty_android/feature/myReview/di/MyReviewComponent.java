package com.tysq.ty_android.feature.myReview.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.myReview.MyReviewFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {MyReviewModule.class},
    dependencies = AppComponent.class
)
public interface MyReviewComponent {
  void inject(MyReviewFragment myReviewFragment);
}
