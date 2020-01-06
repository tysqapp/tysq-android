package com.tysq.ty_android.feature.invite.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.invite.InviteFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {InviteModule.class},
    dependencies = AppComponent.class
)
public interface InviteComponent {
  void inject(InviteFragment inviteFragment);
}
