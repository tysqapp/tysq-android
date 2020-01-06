package com.abc.lib_multi_download.download.task;

import android.os.Handler;
import android.os.Looper;

import com.abc.lib_multi_download.config.DownloadConfig;
import com.abc.lib_multi_download.listener.DownloadListener;
import com.abc.lib_multi_download.model.DownloadInfo;
import com.abc.lib_multi_download.model.status.RunningStatus;
import com.abc.lib_multi_download.thread.IDownloadThread;
import com.abc.lib_log.JLogUtils;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author       : frog
 * time         : 2019-11-29 14:41
 * desc         : 任务基础
 * version      : 1.0.0
 */
public abstract class BaseTask {

    protected static final Object LOCK = new Object();

    private static final Random RANDOM = new Random();
    private static final int BOUND = 1_000_000;

    protected static final Download DOWNLOAD_POOL = new Download();

    protected final DownloadInfo downloadInfo;
    protected final int taskId;
    protected final JLogUtils log;

    public BaseTask(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
        this.taskId = RANDOM.nextInt(BOUND);
        this.log = JLogUtils.getDefault();
    }

    protected static class Download {

        private final ExecutorService mThreadPool;
        private final HashMap<DownloadInfo, IDownloadThread> mDownloadInfoMap;
        private final Handler mHandler;

        Download() {
            mThreadPool = Executors.newFixedThreadPool(DownloadConfig.getInstance().getThreadCount());
            mDownloadInfoMap = new HashMap<>();
            mHandler = new Handler(Looper.getMainLooper());
        }

        ExecutorService getThreadPool() {
            return mThreadPool;
        }

        HashMap<DownloadInfo, IDownloadThread> getDownloadInfoMap() {
            return mDownloadInfoMap;
        }

        void update(final DownloadInfo downloadInfo, final int type) {
            mHandler.post(() -> {

                if (type == RunningStatus.SUCCESS) {
                    DownloadConfig.getInstance().listenerNotification(downloadInfo);
                }

                DownloadListener listener = downloadInfo.getListener();
                if (listener == null) {
                    return;
                }

                switch (type) {
                    case RunningStatus.PAUSE:
                        listener.onPause();
                        break;

                    case RunningStatus.WAITING:
                        listener.onWaiting();
                        break;

                    case RunningStatus.INIT:
                        listener.onInit();
                        break;

                    case RunningStatus.DOWNLOADING:
                        listener.onDownloading();
                        break;

                    case RunningStatus.EXCEPTION:
                        listener.onException();
                        break;

                    case RunningStatus.PROGRESS:
                        listener.onUpdateProgress();
                        break;

                    case RunningStatus.DELETE:
                        listener.onDelete();
                        break;
                }

            });
        }

    }

}
