package com.bit.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bit.presenter.BasePresenter;

/**
 * author       : frog
 * time         : 2019/3/26 下午2:14
 * desc         : 懒加载的fragment
 * version      : 1.3.0
 */
public abstract class BitLazyFragment<T extends BasePresenter> extends BitBaseFragment<T> {

    /**
     * 视图是否已经创建
     */
    private boolean mIsCreateView = false;

    /**
     * 当前的fragment是否可见
     */
    private boolean mIsVisible = false;

    /**
     * 是否已经显示过
     */
    private boolean mIsInit = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (!this.isLazyLoad()) {
            this.mIsVisible = true;
        }

        super.onViewCreated(view, savedInstanceState);

        this.mIsCreateView = true;
        super.logw("onViewCreated：mIsCreateView=" + this.mIsVisible +
                "; mIsVisible=" + this.mIsVisible);

        this.onVisible();
    }

    /**
     * 是否要开启懒加载
     *
     * @return true: 开启懒加载; false: 关闭懒加载
     */
    protected boolean isLazyLoad() {
        return true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.logi("setUserVisibleHint------start");

        super.setUserVisibleHint(isVisibleToUser);

        this.mIsVisible = isVisibleToUser;

        super.logw("setUserVisibleHint：mIsCreateView=" + this.mIsVisible +
                "; mIsVisible=" + this.mIsVisible);

        if (this.mIsVisible) {
            this.onVisible();
        } else {
            this.onHide();
        }

        super.logi("setUserVisibleHint------end");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        logi("onHiddenChanged------start");
        super.onHiddenChanged(hidden);

        this.mIsVisible = !hidden;

        super.logw("onHiddenChanged：mIsCreateView=" + this.mIsVisible +
                "; mIsVisible=" + this.mIsVisible);

        if (this.mIsVisible) {// 在最前端显示 相当于调用了onResume();
            this.onVisible();
        } else {              // 不在最前端显示 相当于调用了onPause();
            this.onHide();
        }

        super.logi("onHiddenChanged------end");
    }

    /**
     * 当前 Fragment 隐藏时调用该方法
     */
    protected void onHide() {
    }

    /**
     * 初始化数据
     * <p>
     * 当{@link BitLazyFragment#hasInitialized()}返回了false，
     * 则说明还没有初始化过数据，则调用该方法；
     * <p>
     * 当{@link BitLazyFragment#hasInitialized()}返回了true，
     * 则说明已经没有初始化过数据，则调用{@link BitLazyFragment#updateData()}；
     */
    protected void initData() {
    }

    /**
     * 更新数据
     * <p>
     * 当{@link BitLazyFragment#hasInitialized()}返回了false，
     * 则说明还没有初始化过数据，则调用{@link BitLazyFragment#initData()}；
     * <p>
     * 当{@link BitLazyFragment#hasInitialized()}返回了true，
     * 则说明已经没有初始化过数据，则调用该方法；
     */
    protected void updateData() {
    }

    /**
     * 每次fragment显示就会调用该方法
     */
    protected void onFragmentVisible() {
    }

    /**
     * 当前 Fragment 可见时调用该方法
     */
    private void onVisible() {
        if (!this.mIsVisible || !this.mIsCreateView) {
            return;
        }

        if (hasInitialized()) {   //已经初始化
            super.logi("updateData");
            this.updateData();
        } else {                  //还未初始化
            super.logi("initData");
            mIsInit = true;
            this.initData();
        }

        super.logw("onFragmentVisible");
        this.onFragmentVisible();
    }

    /**
     * 是否已经初始化，需要用户自行控制
     *
     * @return 默认为false
     */
    protected boolean hasInitialized() {
        return mIsInit;
    }

}
