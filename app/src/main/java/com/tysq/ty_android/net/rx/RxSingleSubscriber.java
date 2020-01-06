package com.tysq.ty_android.net.rx;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public abstract class RxSingleSubscriber<T>
        extends RxBaseSubscriber<T>
        implements SingleObserver<T> {

    public RxSingleSubscriber(BasePresenter basePresenter) {
        super(basePresenter);
    }

    public RxSingleSubscriber(BasePresenter basePresenter, boolean isNeedCheckLogin) {
        super(basePresenter);
        super.isNeedCheckLogin = isNeedCheckLogin;
    }

    @Override
    public void onSubscribe(Disposable d) {
        addDisposable(d);
    }

    @Override
    public void onSuccess(@NonNull T t) {
        onSuccessRes(t);
    }

    @Override
    public void onError(Throwable e) {
        checkThrowable(e);
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
    protected abstract void onSuccessRes(@NonNull T value);
}
