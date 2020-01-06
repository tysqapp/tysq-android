package com.bit.callback;

import com.bit.adapter.BitFrameAdapter;

/**
 * author       : frog
 * time         : 2019-08-23 23:23
 * desc         : 状态页视图回调
 * version      : 1.3.0
 */
public interface StateViewHolderListener {

    /**
     * 空白页回调
     *
     * @param holder
     */
    void handleEmptyViewHolder(BitFrameAdapter.EmptyViewHolder holder);

    /**
     * 重试页回调
     *
     * @param holder
     */
    void handleRetryViewHolder(BitFrameAdapter.RetryViewHolder holder);

    /**
     * 加载页回调
     *
     * @param holder
     */
    void handleLoadingViewHolder(BitFrameAdapter.LoadingViewHolder holder);
}
