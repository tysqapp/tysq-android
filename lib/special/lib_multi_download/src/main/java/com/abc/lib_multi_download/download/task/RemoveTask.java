package com.abc.lib_multi_download.download.task;

import com.abc.lib_multi_download.model.DownloadInfo;
import com.abc.lib_multi_download.thread.IDownloadThread;

/**
 * author       : frog
 * time         : 2019-12-03 14:27
 * desc         : 移除下载线程的任务
 * version      : 1.0.0
 */
public class RemoveTask extends BaseTask implements Runnable {

    public RemoveTask(DownloadInfo downloadInfo) {
        super(downloadInfo);
    }

    @Override
    public void run() {
        synchronized (LOCK) {
            log.title("Remove task NO:" + taskId);
            downloadInfo.log(log);

            IDownloadThread thread = DOWNLOAD_POOL.getDownloadInfoMap().remove(downloadInfo);
            log.content("thread: " + thread);
        }
    }
}
