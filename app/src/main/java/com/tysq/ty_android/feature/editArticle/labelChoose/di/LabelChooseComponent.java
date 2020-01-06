package com.tysq.ty_android.feature.editArticle.labelChoose.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.editArticle.labelChoose.LabelChooseActivity;
import dagger.Component;

@ActivityScope
@Component(
    modules = {LabelChooseModule.class},
    dependencies = AppComponent.class
)
public interface LabelChooseComponent {
  void inject(LabelChooseActivity labelChooseActivity);
}
