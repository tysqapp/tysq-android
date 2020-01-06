package com.abc.lib_multi_download.download.task;

import com.abc.lib_multi_download.model.DownloadInfo;
import com.abc.lib_multi_download.model.status.RunningStatus;
import com.abc.lib_multi_download.thread.IDownloadThread;

/**
 * author       : frog
 * time         : 2019-11-29 17:16
 * desc         : 停止任务
 * version      : 1.0.0
 */
public class PauseTask extends BaseTask implements Runnable {

    public PauseTask(DownloadInfo downloadInfo) {
        super(downloadInfo);
    }

    @Override
    public void run() {
        synchronized (LOCK) {
            log.title("Pause task NO:" + taskId);
            downloadInfo.log(log);

            IDownloadThread thread = DOWNLOAD_POOL.getDownloadInfoMap().get(downloadInfo);
            log.content("thread: " + thread);

            // 没有找到则中止
            if (thread == null) {
                log.showWarn();
                return;
            }

            // 停止线程
            thread.stop(log);
            log.showInfo();

            downloadInfo.setRunningStatus(RunningStatus.PAUSE);
            DOWNLOAD_POOL.update(downloadInfo, RunningStatus.PAUSE);
        }
    }

}
