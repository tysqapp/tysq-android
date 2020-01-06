package com.abc.lib_multi_download.listener;

/**
 * author       : frog
 * time         : 2019-11-27 15:40
 * desc         : 下载回调
 * version      : 1.0.0
 */
public interface DownloadListener {

    /**
     * 暂停回调
     */
    void onPause();

    /**
     * 等待回调
     */
    void onWaiting();

    /**
     * 初始化回调
     */
    void onInit();

    /**
     * 正在下载回调
     */
    void onDownloading();

    /**
     * 更新进度回调
     */
    void onUpdateProgress();

    /**
     * 完成回调
     */
    void onSuccess();

    /**
     * 异常回调
     */
    void onException();

    /**
     * 删除
     */
    void onDelete();

}
