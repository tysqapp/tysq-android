package com.bit.presenter;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.IView;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author a2
 * @date 创建时间：2018/11/14
 * @description
 */
public class BasePresenter<V extends IView> implements IPresenter {

    protected String TAG = this.getClass().getSimpleName();

    private CompositeDisposable mDisposable;
    protected V mView;

    // view 的唯一id
    private String viewId;

    protected BasePresenter mySelf;

    public BasePresenter() {
        this.mySelf = this;
    }

    public BasePresenter(V mView) {
        this.mDisposable = new CompositeDisposable();
        this.viewId = mView.toString();
        this.mView = mView;
        this.mySelf = this;
    }

    public String getViewId() {
        return viewId;
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            // 保证activity结束时取消所有正在执行的订阅
            mDisposable.dispose();
        }
        mDisposable = null;
        mView = null;
        mySelf = null;
    }

    public CompositeDisposable getDisposable() {
        return mDisposable;
    }

    /**
     * 打印错误日志
     *
     * @param code 错误码
     * @param msg  错误信息
     */
    protected void showErrorMsg(int code, String msg) {
        ToastUtils.show(msg + ": " + code);
    }

}
