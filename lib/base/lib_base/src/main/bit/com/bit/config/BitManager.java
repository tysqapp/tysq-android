package com.bit.config;

import com.bit.R;

/**
 * @author a2
 * @date 创建时间：2018/4/21
 * @description
 */

public class BitManager {
    // 默认
    public final static int NONE = -1;
    // 不可点击
    public final static int NO_CLICK = -2;

    private static final BitManager ourInstance = new BitManager();

    public static BitManager getInstance() {
        return ourInstance;
    }

    private BitManager() {
        this.mIsDebug = false;
        this.mLoadingViewLayout = R.layout.bit_state_loading_view;
        this.mRetryViewLayout = R.layout.bit_state_retry_view;
        this.mEmptyViewLayout = R.layout.bit_state_empty_view;

        this.mDialogAnim = R.style.JFrameDialogDefaultAnim;

        this.mToolbarLayout = R.layout.bit_common_tool_bar;
    }

    //是否为debug模式
    private boolean mIsDebug;

    //加载的view的layout
    private int mLoadingViewLayout;
    //重试的view的layout
    private int mRetryViewLayout;
    //空内容的View的layout
    private int mEmptyViewLayout;

    private int mRetryBtnId = NONE;
    private int mEmptyBtnId = NONE;

    //dialog的默认动画
    private int mDialogAnim;

    //app的toolbar的layout
    private int mToolbarLayout;

    public boolean isDebug() {
        return mIsDebug;
    }

    public void setIsDebug(boolean mIsDebug) {
        this.mIsDebug = mIsDebug;
    }

    public int getLoadingViewLayout() {
        return mLoadingViewLayout;
    }

    public void setLoadingViewLayout(int mLoadingViewLayout) {
        this.mLoadingViewLayout = mLoadingViewLayout;
    }

    public int getRetryViewLayout() {
        return mRetryViewLayout;
    }

    public void setRetryViewLayout(int mRetryViewLayout) {
        this.mRetryViewLayout = mRetryViewLayout;
    }

    public int getEmptyViewLayout() {
        return mEmptyViewLayout;
    }

    public void setEmptyViewLayout(int mEmptyViewLayout) {
        this.mEmptyViewLayout = mEmptyViewLayout;
    }

    public int getDialogAnim() {
        return mDialogAnim;
    }

    public void setDialogAnim(int mDialogAnim) {
        this.mDialogAnim = mDialogAnim;
    }

    public int getToolbarLayout() {
        return mToolbarLayout;
    }

    public void setToolbarLayout(int mToolbarLayout) {
        this.mToolbarLayout = mToolbarLayout;
    }

    public int getRetryBtnId() {
        return mRetryBtnId;
    }

    public void setRetryBtnId(int retryBtnId) {
        this.mRetryBtnId = retryBtnId;
    }

    public int getEmptyBtnId() {
        return mEmptyBtnId;
    }

    public void setEmptyBtnId(int emptyBtnId) {
        this.mEmptyBtnId = emptyBtnId;
    }
}
