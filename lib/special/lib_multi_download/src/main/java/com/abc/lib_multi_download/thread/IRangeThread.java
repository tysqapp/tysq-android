package com.abc.lib_multi_download.thread;

import com.abc.lib_log.JLogUtils;

/**
 * author       : frog
 * time         : 2019-11-28 15:57
 * desc         : 分片线程接口
 * version      : 1.0.0
 */
public interface IRangeThread extends Runnable {

    boolean isRunning();

    void stop(JLogUtils log);

    long getDownloadSize();

}
