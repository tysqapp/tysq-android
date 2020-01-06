package com.abc.lib_multi_download.config;

import com.abc.lib_multi_download.listener.SuccessListener;
import com.abc.lib_multi_download.model.DownloadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : frog
 * time         : 2019-11-27 17:41
 * desc         :
 * version      :
 */
public class DownloadConfig {

    private static volatile DownloadConfig instance = null;

    // 3M
    private long rangeSize = 3 * 1024 * 1024;

    // 存储文件夹名称
    private String downloadFile = "jerry_download";

    // 下载线程
    private int threadCount = 3;

    private final List<SuccessListener> successListenerList = new ArrayList<>();

    private DownloadConfig() {
    }

    public static DownloadConfig getInstance() {

        if (instance == null) {
            synchronized (DownloadConfig.class) {
                if (instance == null) {
                    instance = new DownloadConfig();
                }
            }
        }

        return instance;

    }

    public long getRangeSize() {
        return rangeSize;
    }

    public void setRangeSize(long rangeSize) {
        this.rangeSize = rangeSize;
    }

    public String getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(String downloadFile) {
        this.downloadFile = downloadFile;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public synchronized void addListener(SuccessListener listener) {
        successListenerList.add(listener);
    }

    public synchronized void removeListener(SuccessListener listener) {
        successListenerList.remove(listener);
    }

    public synchronized void listenerNotification(DownloadInfo downloadInfo) {
        for (SuccessListener successListener : successListenerList) {
            successListener.onSuccess(downloadInfo);
        }
    }

}
