package com.tysq.ty_android.feature.reportDetail.di;

import com.tysq.ty_android.di.component.AppComponent;
import com.tysq.ty_android.di.scope.ActivityScope;
import com.tysq.ty_android.feature.reportDetail.ReportDetailFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {ReportDetailModule.class},
    dependencies = AppComponent.class
)
public interface ReportDetailComponent {
  void inject(ReportDetailFragment reportDetailFragment);
}
