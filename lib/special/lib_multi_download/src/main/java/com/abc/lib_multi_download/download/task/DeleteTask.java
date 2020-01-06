package com.abc.lib_multi_download.download.task;

import android.text.TextUtils;

import com.abc.lib_multi_download.model.DownloadInfo;
import com.abc.lib_multi_download.model.status.RunningStatus;
import com.abc.lib_multi_download.thread.IDownloadThread;
import com.abc.lib_multi_download.utils.DownloadFileUtils;
import com.abc.lib_utils.FileUtils;

import java.io.File;

/**
 * author       : frog
 * time         : 2019-11-29 17:16
 * desc         : 删除任务
 * version      : 1.0.0
 */
public class DeleteTask extends BaseTask implements Runnable {

    public DeleteTask(DownloadInfo downloadInfo) {
        super(downloadInfo);
    }

    @Override
    public void run() {
        synchronized (LOCK) {

            log.title("Delete task NO: " + taskId);
            downloadInfo.log(log);

            IDownloadThread thread = DOWNLOAD_POOL.getDownloadInfoMap().remove(downloadInfo);
            log.content("thread: " + thread);

            if (thread != null) {
                // 停止线程
                thread.stop(log);
            }

            // 删除分片文件夹
            if (!TextUtils.isEmpty(downloadInfo.getFolderName())) {
                File folder = DownloadFileUtils.getFile(downloadInfo.getFolderName());
                if (folder.exists()) {
                    boolean isSuc = FileUtils.deleteFolder(folder);
                    log.content("delete folder result: " + isSuc);
                }
            }

            // 删除拼凑文件
            if (!TextUtils.isEmpty(downloadInfo.getTmpFileName())) {
                File tmp = DownloadFileUtils.getFile(downloadInfo.getTmpFileName());
                if (tmp.exists()) {
                    boolean isDelete = tmp.delete();
                    log.content("delete tmp result: " + isDelete);
                }
            }

            // 删除最终文件
            if (!TextUtils.isEmpty(downloadInfo.getFileName())) {
                File file = DownloadFileUtils.getFile(downloadInfo.getFileName());
                if (file.exists()) {
                    boolean isDelete = file.delete();
                    log.content("delete result result: " + isDelete);
                }
            }

            // 删除数据库
            boolean isDelete = downloadInfo.delete();
            log.content("delete DB result: " + isDelete);
            log.showInfo();

            // 更新视图
            DOWNLOAD_POOL.update(downloadInfo, RunningStatus.DELETE);
        }
    }
}
