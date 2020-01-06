package com.tysq.ty_android.net.rx;

import com.bit.presenter.BasePresenter;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.NetCode;
import com.tysq.ty_android.feature.login.LoginActivity;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.net.ApiStatus;
import com.tysq.ty_android.net.exception.ServerException;

import java.io.IOException;
import java.lang.ref.WeakReference;

import cache.User;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * author       : frog
 * time         : 2018-11-20 11:01
 * desc         :
 * version      : 1.3.0
 */

public abstract class RxBaseSubscriber<T> {

    private static final String UNKNOWN_MSG = "网络异常，请稍后再试";
    private static final String SEVER_ERROR_MSG = "服务器开小差";

    private WeakReference<BasePresenter> mBasePresenter;
    protected boolean isNeedCheckLogin;

    public RxBaseSubscriber(BasePresenter basePresenter) {
        this.mBasePresenter = new WeakReference<>(basePresenter);
        this.isNeedCheckLogin = true;
    }

    protected void checkThrowable(Throwable e) {
        e.printStackTrace();

        if (e instanceof HttpException) {
            ResponseBody body = ((HttpException) e).response().errorBody();
            int code = ((HttpException) e).code();

            //400-499 的 错误
            if (code >= 400 && code <= 499) {
                onError(code, SEVER_ERROR_MSG);
                return;
            }

            //500-599 的 错误
            if (code >= 500 && code <= 505) {
                onError(code, SEVER_ERROR_MSG);
                return;
            }

            //空响应体
            if (body == null) {
                onError(code, UNKNOWN_MSG);
                return;
            }

            try {
                onError(code, body.string());
            } catch (IOException e1) {
                onError(code, UNKNOWN_MSG);
            }

        } else if (e instanceof ServerException) {

            ServerException exception = (ServerException) e;

            if (exception.getCode() == NetCode.LOGIN_ERROR_CODE
                    || exception.getCode() == NetCode.LOGIN_EXPIRE_ERROR_CODE) {

                if (isNeedCheckLogin) {
                    User user = UserCache.getDefault();
                    LoginActivity.startActivityForInvalidate(TyApplication.getContext(),
                            user == null ? "" : user.getEmail(), mBasePresenter.get().getViewId());
                }

            }

            onError(exception.getCode(), exception.getMessage());
        } else {
            onError(ApiStatus.UNKNOWN, UNKNOWN_MSG);
        }
    }

    void addDisposable(Disposable d) {
        BasePresenter basePresenter = mBasePresenter.get();
        if (basePresenter != null) {
            basePresenter.getDisposable().add(d);
        }
    }

    /**
     * 错误回调
     */
    protected abstract void onError(int code, String message);

    /**
     * 成功回调
     *
     * @param value
     */
    protected abstract void onSuccessRes(T value);

}
