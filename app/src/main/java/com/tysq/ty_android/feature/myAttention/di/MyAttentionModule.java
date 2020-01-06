package com.tysq.ty_android.feature.myAttention.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.myAttention.MyAttentionView;
import dagger.Module;
import dagger.Provides;

@Module
public final class MyAttentionModule {
  private final MyAttentionView mView;

  public MyAttentionModule(MyAttentionView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public MyAttentionView getMyAttentionView() {
    return this.mView;
  }
}
