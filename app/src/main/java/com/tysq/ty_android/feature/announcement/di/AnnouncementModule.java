package com.tysq.ty_android.feature.announcement.di;

import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.announcement.AnnouncementView;
import dagger.Module;
import dagger.Provides;

@Module
public final class AnnouncementModule {
  private final AnnouncementView mView;

  public AnnouncementModule(AnnouncementView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public AnnouncementView getAnnouncementView() {
    return this.mView;
  }
}
