package com.tysq.ty_android.login;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author       : frog
 * time         : 2017-10-16 10:56
 * desc         :
 * version      : 1.3.0
 */

//用于方法
@Target(ElementType.METHOD)
//运行时
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLogin {
}
