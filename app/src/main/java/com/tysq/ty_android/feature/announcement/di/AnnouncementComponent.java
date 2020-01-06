package com.tysq.ty_android.feature.announcement.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.announcement.AnnouncementFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {AnnouncementModule.class},
    dependencies = AppComponent.class
)
public interface AnnouncementComponent {
  void inject(AnnouncementFragment announcementFragment);
}
