package com.tysq.ty_android.net.rx;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class RxObservableSubscriber<T> extends RxBaseSubscriber<T> implements Observer<T> {

    public RxObservableSubscriber(BasePresenter basePresenter) {
        super(basePresenter);
    }

    @Override
    public void onSubscribe(Disposable d) {
        addDisposable(d);
    }

    @Override
    public void onNext(@NonNull T t) {
        onSuccessRes(t);
    }

    @Override
    public void onError(Throwable e) {
        checkThrowable(e);
    }

    @Override
    public void onComplete() {

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
