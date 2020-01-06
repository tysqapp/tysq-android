package com.tysq.ty_android.feature.rank.myRank.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.rank.myRank.MyRankFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {MyRankModule.class},
    dependencies = AppComponent.class
)
public interface MyRankComponent {
  void inject(MyRankFragment myRankFragment);
}
