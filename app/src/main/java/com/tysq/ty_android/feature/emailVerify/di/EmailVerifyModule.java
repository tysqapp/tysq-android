package com.tysq.ty_android.feature.emailVerify.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.emailVerify.EmailVerifyView;
import dagger.Module;
import dagger.Provides;

@Module
public final class EmailVerifyModule {
  private final EmailVerifyView mView;

  public EmailVerifyModule(EmailVerifyView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public EmailVerifyView getEmailVerifyView() {
    return this.mView;
  }
}
