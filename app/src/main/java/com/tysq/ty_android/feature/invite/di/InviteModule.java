package com.tysq.ty_android.feature.invite.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.invite.InviteView;
import dagger.Module;
import dagger.Provides;

@Module
public final class InviteModule {
  private final InviteView mView;

  public InviteModule(InviteView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public InviteView getInviteView() {
    return this.mView;
  }
}
