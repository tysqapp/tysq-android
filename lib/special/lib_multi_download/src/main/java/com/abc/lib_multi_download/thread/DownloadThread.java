package com.abc.lib_multi_download.thread;

import android.os.Handler;
import android.os.Looper;

import com.abc.lib_multi_download.config.DownloadConfig;
import com.abc.lib_multi_download.downloadStrategy.ContinueStrategy;
import com.abc.lib_multi_download.downloadStrategy.FirstStrategy;
import com.abc.lib_multi_download.downloadStrategy.IStrategy;
import com.abc.lib_multi_download.exception.DownloadException;
import com.abc.lib_multi_download.jerry.JerryDownload;
import com.abc.lib_multi_download.listener.DownloadListener;
import com.abc.lib_multi_download.model.DownloadInfo;
import com.abc.lib_multi_download.model.RangeInfo;
import com.abc.lib_multi_download.model.status.RunningStatus;
import com.abc.lib_multi_download.model.status.Status;
import com.abc.lib_multi_download.utils.BitUtils;
import com.abc.lib_multi_download.utils.DateUtils;
import com.abc.lib_multi_download.utils.DownloadFileUtils;
import com.abc.lib_log.JLogUtils;
import com.abc.lib_utils.CloseUtils;
import com.abc.lib_utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * author       : frog
 * time         : 2019-11-27 14:12
 * desc         : 下载线程
 * version      : 1.0.0
 */
public class DownloadThread extends Thread implements IDownloadThread {

    // 分片线程数量
    private static final int RANGE_THREAD_NUM = 3;
    // 进度更新间隔 1秒
    private final static int PROCESS_UPDATE_TIME_OFFSET = 1000;

    // 是否运行
    private volatile boolean isRunning;
    // 任务id
    private final int taskId;
    // 日志
    private final JLogUtils log;
    // 下载信息
    private final DownloadInfo downloadInfo;
    // 需要下载任务
    private final Queue<RangeInfo> needDownloadList;
    // 分片线程
    private final List<IRangeThread> rangeThreadList;

    // 已经完成的大小
    private long downloadSize;

    // 是否循环更新进度
    private volatile boolean isLoopToUpdateProgress = true;

    // 初始化策略
    private volatile IStrategy strategy;

    // 切换主线程
    private Handler handler;

    public DownloadThread(int taskId,
                          DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
        this.isRunning = true;
        this.taskId = taskId;
        this.log = JLogUtils.getDefault();
        this.rangeThreadList = new ArrayList<>();
        this.needDownloadList = new ArrayDeque<>();
        this.downloadSize = 0;
        this.handler = new Handler(Looper.getMainLooper());

        log.title("Download task NO: " + taskId)
                .content("create time: " + DateUtils.getCurTime(System.currentTimeMillis()));
    }

    @Override
    public void run() {

        // 如果已经关闭，则直接终止 【中止1】
        if (!isRunning) {
            pause("stop at [1]");
            return;
        }

        log.content("start init......");
        downloadInfo.setRunningStatus(RunningStatus.INIT);
        update(RunningStatus.INIT);

        // 进行策略
        if (BitUtils.isContain(downloadInfo.getStatus(), Status.INIT)) {
            log.content("Run init logic.");
            strategy = new FirstStrategy(downloadInfo);
        } else {
            log.content("Run continue logic.");
            strategy = new ContinueStrategy(downloadInfo);
        }

        try {
            boolean isSuccess = strategy.run();
            log.content("strategy result: " + isSuccess);
        } catch (DownloadException e) {
            e.printStackTrace();
            error(e.getMessage(), e.getTip());
            return;
        }

        //【中止2】
        if (!isRunning) {
            pause("stop at [2]");
            return;
        }

        downloadInfo.setRangeFolder(DownloadFileUtils.getFile(downloadInfo.getFolderName()));

        // 进行初始化可下载的分片
        initRange();

        //【中止3】
        if (!isRunning) {
            pause("stop at [3]");
            return;
        }

        // 检测分片是否请求完毕
        boolean isRangeReqOver = checkRangeReqOver();

        // 分片请求完成，要进行分片合并
        if (isRangeReqOver) {
            try {
                assembleRange();
            } catch (DownloadException e) {
                e.printStackTrace();

                deleteTmpFile();

                error(e.getMessage(), e.getTip());
                return;
            }

            //【中止4】
            if (!isRunning) {
                pause("stop at [4]");
                return;
            }

            // 拼接完成，结束
            downloadFinish();

            // 通知成功
            success();

            downloadInfo.log(log);
            log.showInfo();
            clean();

            return;
        }

        // 开始下载
        downloadInfo.setRunningStatus(RunningStatus.DOWNLOADING);
        update(RunningStatus.DOWNLOADING);

        synchronized (this) {
            // 开启分片分任务
            for (int i = 0; i < RANGE_THREAD_NUM; ++i) {
                RangeInfo rangeInfo = getRangeTask(0);

                // 说明没有任务
                if (rangeInfo == null) {
                    break;
                }

                IRangeThread rangeThread = new RangeThread(taskId, this, rangeInfo);
                rangeThreadList.add(rangeThread);

                // 开启线程
                new Thread(rangeThread).start();
            }
        }

        downloadInfo.log(log);
        log.content("start progress").showInfo();
        log.clear();
        log.title("Download task NO: " + taskId);

        // 循环获取进度
        while (isLoopToUpdateProgress) {
            try {
                sleep(PROCESS_UPDATE_TIME_OFFSET);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int progress = calculateProgress();
            if (progress >= 98) {
                progress = 98;
            }
            // 更新下进度
            downloadInfo.setProgress(progress);
            downloadInfo.setDownloadSize(downloadSize);
            downloadInfo.update();

            update(RunningStatus.PROGRESS);
        }

        // 是否下载结束
        boolean isOver = true;
        synchronized (this) {
            if (rangeThreadList.size() > 0) {
                isOver = false;
            } else if (needDownloadList.size() > 0) {
                isOver = false;
            }
        }

        // 未结束，说明异常
        if (!isOver) {
            // 如果包含了异常错误，说明分片线程有问题
            if (BitUtils.isContain(downloadInfo.getStatus(), Status.EXCEPTION)) {
                update(RunningStatus.EXCEPTION);
            } else {
                update(RunningStatus.PAUSE);
            }

            downloadInfo.log(log);
            log.showWarn();
            clean();
            return;
        }

        // 进行拼接
        try {
            assembleRange();
        } catch (DownloadException e) {
            e.printStackTrace();
            deleteTmpFile();
            error(e.getMessage(), e.getTip());
            return;
        }

        //【中止5】
        if (!isRunning) {
            pause("stop at [5]");
            return;
        }

        downloadInfo.setProgress(100);
        update(RunningStatus.PROGRESS);

        // 拼接完成，结束
        downloadFinish();

        // 线程睡眠五百毫秒，让用户看到完成
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 通知成功
        success();
        log.title("Download thread NO:" + taskId + " success.");
        clean();
    }

    /**
     * 删除临时文件
     */
    private void deleteTmpFile() {
        String tmp = downloadInfo.getTmpFileName();

        File file = DownloadFileUtils.getFile(tmp);
        file.deleteOnExit();

    }

    /**
     * 计算进度
     */
    private synchronized int calculateProgress() {
        long curLength = downloadSize;
        for (IRangeThread rangeThread : rangeThreadList) {
            long rangeDownloadSize = rangeThread.getDownloadSize();
            curLength += rangeDownloadSize;
        }

        downloadSize = curLength;
        downloadInfo.setDownloadSize(curLength);
        return (int) (curLength * 100 / downloadInfo.getTotalSize());
    }

    /**
     * 拼接结束
     */
    private void downloadFinish() {
        File tmpFileName = DownloadFileUtils.getFile(downloadInfo.getTmpFileName());
        boolean isSuc = FileUtils.renameFileAtTheSameFolder(tmpFileName, downloadInfo.getFileName());

        if (!isSuc) {
            error("文件异常", "Rename to success file failure.");
            return;
        }

        downloadInfo.setStatus(Status.SUCCESS);
        downloadInfo.setRunningStatus(RunningStatus.SUCCESS);
        downloadInfo.update();

        // 删除分片文件
        File folder = DownloadFileUtils.getFile(downloadInfo.getFolderName());
        boolean isDeleteSuc = FileUtils.deleteFolder(folder);

        log.content("delete range file result:" + isDeleteSuc);
    }

    /**
     * 组装分片
     */
    private void assembleRange() throws DownloadException {

        // 中止运行
        if (!isRunning) {
            return;
        }

        String tmp = downloadInfo.getTmpFileName();

        File tmpFile = DownloadFileUtils.getFile(tmp);
        // 文件存在 且 文件长度大于0，则删除
        if (tmpFile.exists() && tmpFile.length() > 0) {
            boolean delete = tmpFile.delete();
            // 删除失败
            if (!delete) {
                throw new DownloadException("文件异常", "Tmp file delete failure.");
            }
        }

        // 创建临时文件
        tmpFile = DownloadFileUtils.createFile(tmp);

        if (tmpFile == null) {
            throw new DownloadException("文件异常", "Tmp file create failure.");
        }

        long totalSize = downloadInfo.getTotalSize();
        long rangeSize = downloadInfo.getRangeSize();

        // 计算分片个数
        long count = totalSize / rangeSize;
        if (totalSize % rangeSize > 0) {
            count += 1;
        }

        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(tmpFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            CloseUtils.close(outputStream);
            throw new DownloadException("文件异常", "BufferedOutputStream create failure.");
        }

        // 进行每一片的获取和拼接
        for (int i = 0; i < count; ++i) {
            // 中止运行
            if (!isRunning) {
                CloseUtils.close(outputStream);
                return;
            }

            long start = i * rangeSize;

            long end;
            // 如果是最后一个
            if (i == count - 1) {
                end = totalSize - 1;
            } else {
                end = (i + 1) * rangeSize - 1;
            }

            String range = start + "-" + end;

            File curRangeFile = new File(downloadInfo.getRangeFolder(), range);

            boolean lengthLegal = false;

            // 如果存在，则检查其长度是否合理
            if (curRangeFile.exists()) {
                long length = curRangeFile.length();

                long existLength = end - start + 1;

                // 如果分片所需长度和真实存在的长度相同，则说明合法
                lengthLegal = existLength == length;
            }

            if (!lengthLegal) {
                CloseUtils.close(outputStream);
                throw new DownloadException("文件异常", "File length is illegal!");
            }

            InputStream inputStream = null;
            try {
                inputStream = new BufferedInputStream(new FileInputStream(curRangeFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                CloseUtils.close(inputStream);
                CloseUtils.close(outputStream);
                throw new DownloadException("文件异常", "InputStream open failure.");
            }

            boolean isSuccess = FileUtils.transmitStream(inputStream, outputStream, false);

            CloseUtils.close(inputStream);

            if (!isSuccess) {
                CloseUtils.close(outputStream);
                throw new DownloadException("文件异常", "Assemble is failure.");
            }
        }

        CloseUtils.close(outputStream);
    }

    /**
     * 检查所有的分片是否都已经处理完毕
     */
    private synchronized boolean checkRangeReqOver() {
        for (IRangeThread rangeThread : rangeThreadList) {
            if (rangeThread.isRunning()) {
                return false;
            }
        }

        return needDownloadList.size() <= 0;
    }

    /**
     * 初始化分片
     */
    private void initRange() {

        long totalSize = downloadInfo.getTotalSize();
        long rangeSize = downloadInfo.getRangeSize();

        // 计算分片个数
        long count = totalSize / rangeSize;
        if (totalSize % rangeSize > 0) {
            count += 1;
        }

        for (int i = 0; i < count; ++i) {

            if (!isRunning) {
                return;
            }

            long start = i * rangeSize;

            long end;
            // 如果是最后一个
            if (i == count - 1) {
                end = totalSize - 1;
            } else {
                end = (i + 1) * rangeSize - 1;
            }

            String range = start + "-" + end;

            File curRangeFile = new File(downloadInfo.getRangeFolder(), range);

            boolean lengthLegal = false;

            // 如果存在，则检查其长度是否合理
            if (curRangeFile.exists()) {
                long length = curRangeFile.length();

                long existLength = end - start + 1;

                // 如果分片所需长度和真实存在的长度相同，则说明合法
                lengthLegal = existLength == length;

                downloadSize += existLength;
            }

            // 如果是合法的，说明分片文件存在，且长度合理，则可以继续下一片的检查
            if (lengthLegal) {
                continue;
            }

            long realStart;
            // 如果分片是存在的，说明是没有完全下载完的，需要调整其起始位置
            if (curRangeFile.exists()) {
                realStart = start + curRangeFile.length();
            } else {
                realStart = start;
            }

            RangeInfo rangeInfo = new RangeInfo();
            rangeInfo.setStart(start);
            rangeInfo.setEnd(end);
            rangeInfo.setReqRange("bytes=" + realStart + "-" + end);
            rangeInfo.setRangeFileName(range);
            rangeInfo.setFolderName(downloadInfo.getFolderName());
            rangeInfo.setUrl(downloadInfo.getUrl());

            // 加入任务
            putRangeTask(rangeInfo);
        }

        downloadInfo.setProgress(downloadSize * 100 / totalSize);
        downloadInfo.setDownloadSize(downloadSize);
        downloadInfo.update();

    }

    /**
     * 停止线程
     */
    @Override
    public synchronized void stop(JLogUtils log) {
        log.content("download thread to stop.")
                .content("is running: " + isRunning);

        if (!isRunning) {
            return;
        }

        isRunning = false;
        // 停止初始化策略
        if (strategy != null) {
            strategy.stop(log);
        }

        log.content("stop range thread:" + rangeThreadList.size());
        for (IRangeThread rangeThread : rangeThreadList) {
            log.param("rangeThread: " + rangeThread);
            rangeThread.stop(log);
        }

    }

    /**
     * 是否正在运行
     *
     * @return true：正在运行；false：停止
     */
    public synchronized boolean isRunning() {
        return isRunning;
    }

    /**
     * 错误
     *
     * @param msg 错误信息
     * @param tip 提示信息
     */
    private void error(String msg,
                       String tip) {

        downloadInfo.setRunningStatus(RunningStatus.EXCEPTION);

        // 添加异常状态
        int status = downloadInfo.getStatus();
        status = BitUtils.add(status, Status.EXCEPTION);
        downloadInfo.setStatus(status);

        downloadInfo.setMsg(msg);
        boolean updateResult = downloadInfo.update();

        downloadInfo.log(log);

        log.content("update result: " + updateResult)
                .content("Msg: " + msg)
                .content("Tip: " + tip)
                .showError();

        clean();

        update(RunningStatus.EXCEPTION);
    }

    /**
     * 成功通知
     */
    private void success() {
        downloadInfo.setRunningStatus(RunningStatus.SUCCESS);
        update(RunningStatus.SUCCESS);
    }

    /**
     * 暂停
     */
    private void pause(String tip) {
        downloadInfo.log(log);
        log.content(tip).showWarn();
        clean();
    }

    /**
     * 获取分片任务
     *
     * @return 分片任务
     */
    public synchronized RangeInfo getRangeTask(long rangeDownloadSize) {
        this.downloadSize += rangeDownloadSize;
        this.downloadInfo.setDownloadSize(downloadSize);
        return needDownloadList.poll();
    }

    /**
     * 存放分片任务
     *
     * @param rangeInfo 分片任务
     */
    public synchronized void putRangeTask(RangeInfo rangeInfo) {
        needDownloadList.offer(rangeInfo);
    }

    /**
     * 分片线程结束，需要通知主线程
     *
     * @param rangeThread 分片线程
     * @param isSuc       是否正常结束
     */
    @Override
    public synchronized void onRangeTaskOver(IRangeThread rangeThread,
                                             DownloadThread downloadThread,
                                             boolean isSuc,
                                             RangeInfo rangeInfo,
                                             String msg,
                                             String tip) {
        rangeThreadList.remove(rangeThread);

        // 如果不正常，则将其他的线程进行停止
        if (!isSuc) {
            log.content(tip);

            int status = downloadInfo.getStatus();
            status = BitUtils.add(status, Status.EXCEPTION);
            downloadInfo.setStatus(status);
            downloadInfo.setMsg(msg);

            downloadInfo.setRunningStatus(RunningStatus.EXCEPTION);

            downloadInfo.update();

            for (IRangeThread otherThread : rangeThreadList) {
                otherThread.stop(log);
            }

            needDownloadList.add(rangeInfo);
        }

        if (rangeThreadList.size() > 0) {
            return;
        }

        // 取消进度更新同时唤醒下载线程
        isLoopToUpdateProgress = false;
        if (!downloadThread.isInterrupted()) {
            downloadThread.interrupt();
        }
    }

    /**
     * 清空对象
     */
    private synchronized void clean() {
        JerryDownload.getInstance()._remove(downloadInfo);
        rangeThreadList.clear();
        needDownloadList.clear();
        log.clear();
    }

    /**
     * 通知视图
     *
     * @param type 更新类型{@link RunningStatus}
     */
    private void update(final int type) {
        handler.post(() -> {

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
                    if (downloadInfo.getRunningStatus() == RunningStatus.PAUSE
                            || downloadInfo.getRunningStatus() == RunningStatus.EXCEPTION) {
                        return;
                    }
                    listener.onUpdateProgress();
                    break;

                case RunningStatus.DELETE:
                    listener.onDelete();
                    break;
            }

        });
    }

    @Override
    public String toString() {
        return "DownloadThread{" +
                "isRunning=" + isRunning +
                ", taskId=" + taskId +
                ", downloadSize=" + downloadSize +
                '}';
    }
}
