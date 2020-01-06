package com.abc.lib_multi_download.downloadStrategy;

import com.abc.lib_multi_download.model.DownloadInfo;
import com.abc.lib_log.JLogUtils;
import com.abc.lib_utils.CloseUtils;

import okhttp3.Response;

/**
 * author       : frog
 * time         : 2019-10-14 11:07
 * desc         : 首次传输策略
 * version      : 1.0.0
 */
abstract class BaseStrategy implements IStrategy {

    boolean isRunning;
    DownloadInfo mDownloadInfo;
    Response mResponse;

    BaseStrategy(DownloadInfo downloadInfo) {
        this.mDownloadInfo = downloadInfo;
        this.isRunning = true;
    }

    public void stop(JLogUtils log) {
        log.content("Strategy stop: " + isRunning);

        if (!isRunning) {
            return;
        }
        isRunning = false;
        CloseUtils.close(mResponse);
    }
}
