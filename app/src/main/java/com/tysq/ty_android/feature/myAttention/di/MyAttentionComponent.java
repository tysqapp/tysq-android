package com.tysq.ty_android.feature.myAttention.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.myAttention.MyAttentionFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {MyAttentionModule.class},
    dependencies = AppComponent.class
)
public interface MyAttentionComponent {
  void inject(MyAttentionFragment myAttentionFragment);
}
