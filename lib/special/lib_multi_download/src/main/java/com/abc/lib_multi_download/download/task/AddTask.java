package com.abc.lib_multi_download.download.task;

import com.abc.lib_multi_download.model.DownloadInfo;
import com.abc.lib_multi_download.model.status.RunningStatus;
import com.abc.lib_multi_download.model.status.Status;
import com.abc.lib_multi_download.thread.DownloadThread;
import com.abc.lib_multi_download.utils.BitUtils;

import java.util.Set;

/**
 * author       : frog
 * time         : 2019-11-29 10:48
 * desc         : 添加下载任务
 * version      : 1.0.0
 */
public class AddTask extends BaseTask implements Runnable {

    public AddTask(DownloadInfo downloadInfo) {
        super(downloadInfo);
    }

    @Override
    public void run() {
        synchronized (LOCK) {
            log.title("Add task NO:" + taskId);

            // 下载信息是否存在
            boolean infoExists = downloadInfo.exists();
            log.add("downloadInfo exist: " + infoExists);

            // 不存在数据库，进行保存
            if (!infoExists) {
                boolean save = downloadInfo.save();
                log.add("downloadInfo 入库: " + save);

                // 如果没有保存成功
                if (!save) {
                    log.showError();
                    return;
                }
            }

            // 如果已经存在的话，则需要检查一下是否已经开启
            if (infoExists) {
                Set<DownloadInfo> downloadInfoSet = DOWNLOAD_POOL.getDownloadInfoMap().keySet();
                for (DownloadInfo item : downloadInfoSet) {
                    if (item.getId() == downloadInfo.getId()) {

                        downloadInfo.log(log);
                        log.content("DOWNLOAD_POOL has the same DOWNLOAD INFO!!!").showError();

                        return;
                    }
                }
            }

            log.content("Add the new download task.");

            // 如果添加了完成状态的任务，则终止
            if (BitUtils.isContain(downloadInfo.getStatus(), Status.SUCCESS)) {

                downloadInfo.log(log);
                log.content("It's a success status").showWarn();
                DOWNLOAD_POOL.update(downloadInfo, RunningStatus.SUCCESS);

                return;
            }

            // 需要将错误的状态移除，然后保存
            // 因为查询的 Task 会根据 Status 进行初始化各种状态
            int originalStatus = downloadInfo.getStatus();
            originalStatus = BitUtils.remove(originalStatus, Status.EXCEPTION);
            downloadInfo.setStatus(originalStatus);
            boolean update = downloadInfo.update();
            log.add("downloadInfo 更新状态是否成功: " + update);

            // 将运行是状态设置为先通知在等待
            downloadInfo.setRunningStatus(RunningStatus.WAITING);
            DOWNLOAD_POOL.update(downloadInfo, RunningStatus.WAITING);

            DownloadThread downloadThread = new DownloadThread(taskId, downloadInfo);
            DOWNLOAD_POOL.getDownloadInfoMap().put(downloadInfo, downloadThread);
            DOWNLOAD_POOL.getThreadPool().submit(downloadThread);

            downloadInfo.log(log);
            log.showInfo();
        }
    }
}
