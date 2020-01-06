package com.tysq.ty_android.login;

import android.util.Log;

import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.login.LoginActivity;
import com.tysq.ty_android.local.sp.UserCache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * author       : frog
 * time         : 2018/11/30
 * desc         : 登陆的切面
 * version      : 1.3.0
 */
@Aspect
public class CheckLoginAspectJ {

    private String TAG = CheckLoginAspectJ.class.getSimpleName();

    @Pointcut("execution(@com.tysq.ty_android.login.CheckLogin * *(..))")
    public void executeCheckLogin() {
        Log.i(TAG, "executeCheckLogin: ");
    }

    @Around("executeCheckLogin()")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckLogin checkLogin = signature.getMethod().getAnnotation(CheckLogin.class);
        if (checkLogin != null) {

            if (UserCache.isEmpty()) {
                LoginActivity.startActivityForAOP(TyApplication.getContext(), "");
                return null;
            }

        }

        return joinPoint.proceed();

    }

}
