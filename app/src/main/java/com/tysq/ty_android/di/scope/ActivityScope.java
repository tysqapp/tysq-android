package com.tysq.ty_android.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * author       : frog
 * time         : 2019/4/11 下午3:48
 * desc         : activity的scope
 * version      : 1.3.0
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}