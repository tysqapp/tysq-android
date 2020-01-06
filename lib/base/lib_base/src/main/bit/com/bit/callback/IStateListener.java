package com.bit.callback;

/**
 * author       : frog
 * time         : 2019/3/26 下午2:45
 * desc         : 状态回调
 * version      : 1.3.0
 */
public interface IStateListener {

    /**
     * 重试回调
     */
    void onRetry();

    /**
     * 加载回调
     */
    void onLoading();

    /**
     * 无数据回调
     */
    void onEmpty();

}
