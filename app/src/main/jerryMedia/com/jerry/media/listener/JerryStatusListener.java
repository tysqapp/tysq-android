package com.jerry.media.listener;

/**
 * author       : frog
 * time         : 2019-06-27 11:06
 * desc         : 状态监听器
 * version      : 1.3.0
 */
public interface JerryStatusListener {

    int LOADING = 21;
    int ERROR = 22;

    /**
     * 状态改变回调
     */
    void playStatusChange();

    /**
     * 进度变动回调
     */
    void progressChange();

    /**
     * 提示回调
     */
    void tipChange(int code, String msg);

}
