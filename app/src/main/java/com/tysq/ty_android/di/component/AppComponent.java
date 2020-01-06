package com.tysq.ty_android.di.component;

import android.app.Application;
import android.content.Context;


import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.di.module.AppModule;
import com.tysq.ty_android.feature.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * author       : xxxx
 * time         : 2018-11-08 10:09
 * desc         :
 * version      : 1.3.0
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Application application();

    Context context();

    void inject(TyApplication appApplication);

    void inject(MainActivity mainActivity);

}
