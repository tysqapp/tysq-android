package com.bit.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.bit.config.BitManager;

/**
 * @author a2
 * @date 创建时间：2018/4/21
 * @description 各种状态的layout
 */

public class StateLayout extends FrameLayout {

    private static final String TAG = StateLayout.class.getSimpleName();

    //加载的view
    private View mLoadingView;
    //重试的view
    private View mRetryView;
    //空内容的View
    private View mEmptyView;
    //内容View
    private View mContentView;

    private LayoutInflater mInflater;

    public StateLayout(Context context) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
    }

    //是否正在刷新
    public boolean isLoading() {
        return this.mLoadingView != null && this.mLoadingView.getVisibility() == VISIBLE;
    }

    public void showLoading() {
        showView(mLoadingView);
    }

    public void showRetry() {
        showView(mRetryView);
    }

    public void showContent() {
        showView(mContentView);
    }

    public void showEmpty() {
        showView(mEmptyView);
    }

    //显示参数中的view
    private void showView(View stateView) {
        if (mRetryView != null) {
            mRetryView.setVisibility(stateView == mRetryView ? VISIBLE : GONE);
        }
        if (mContentView != null) {
            mContentView.setVisibility(stateView == mContentView ? VISIBLE : GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(stateView == mEmptyView ? VISIBLE : GONE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(stateView == mLoadingView ? VISIBLE : GONE);
        }
    }

    public View setContentView(int layoutId) {
        return setContentView(this.mInflater.inflate(layoutId, this, false));
    }

    public View setLoadingView(int layoutId) {
        return setLoadingView(this.mInflater.inflate(layoutId, this, false));
    }

    public View setEmptyView(int layoutId) {
        return setEmptyView(this.mInflater.inflate(layoutId, this, false));
    }

    public View setRetryView(int layoutId) {
        return setRetryView(this.mInflater.inflate(layoutId, this, false));
    }

    public View setLoadingView(View view) {
        View loadingView = this.mLoadingView;
        if (loadingView != null && BitManager.getInstance().isDebug()) {
            Log.w(TAG, "you have already set a loading view and would be instead of this new one.");
        }
        removeView(loadingView);
        addView(view);
        mLoadingView = view;
        return mLoadingView;
    }

    public View setEmptyView(View view) {
        View emptyView = this.mEmptyView;
        if (emptyView != null && BitManager.getInstance().isDebug()) {
            Log.w(TAG, "you have already set a empty view and would be instead of this new one.");
        }
        removeView(emptyView);
        addView(view);
        this.mEmptyView = view;
        return this.mEmptyView;
    }

    public View setRetryView(View view) {
        View retryView = this.mRetryView;
        if (retryView != null && BitManager.getInstance().isDebug()) {
            Log.w(TAG, "you have already set a retry view and would be instead of this new one.");
        }
        removeView(retryView);
        addView(view);
        this.mRetryView = view;
        return this.mRetryView;

    }

    public View setContentView(View view) {
        View contentView = this.mContentView;
        if (contentView != null && BitManager.getInstance().isDebug()) {
            Log.w(TAG, "you have already set a content view and would be instead of this new one.");
        }
        removeView(contentView);
        addView(view);
        this.mContentView = view;
        return this.mContentView;
    }

    public View getRetryView() {
        return this.mRetryView;
    }

    public View getLoadingView() {
        return this.mLoadingView;
    }

    public View getContentView() {
        return this.mContentView;
    }

    public View getEmptyView() {
        return this.mEmptyView;
    }

}
