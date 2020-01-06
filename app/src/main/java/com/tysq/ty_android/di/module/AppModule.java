package com.tysq.ty_android.di.module;

import android.app.Application;
import android.content.Context;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.app.TyApplication;

import dagger.Module;
import dagger.Provides;

/**
 * author       : frog
 * time         : 2019/4/11 下午4:12
 * desc         :
 * version      : 1.3.0
 */
@Module
public class AppModule {
    private final TyApplication mAppApplication;

    public AppModule(TyApplication mAppApplication) {
        this.mAppApplication = mAppApplication;
    }

    @Provides
    public Application provideApplication() {
        return mAppApplication;
    }

    @Provides
    public Context provideContext() {
        return mAppApplication.getApplicationContext();
    }

    @Provides
    public BasePresenter provideBasePresenter() {
        return new BasePresenter();
    }

}
