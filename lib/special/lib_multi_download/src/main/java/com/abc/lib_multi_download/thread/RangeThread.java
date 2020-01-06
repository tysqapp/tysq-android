package com.abc.lib_multi_download.thread;

import com.abc.lib_multi_download.constants.ReqHead;
import com.abc.lib_multi_download.model.RangeInfo;
import com.abc.lib_multi_download.utils.DownloadFileUtils;
import com.abc.lib_multi_download.utils.NetUtils;
import com.abc.lib_log.JLogUtils;
import com.abc.lib_utils.CloseUtils;
import com.abc.lib_utils.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Random;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * author       : frog
 * time         : 2019-11-27 14:12
 * desc         : 分片线程
 * version      :
 */
public class RangeThread implements IRangeThread {

    private static final int BOUND = 10000;
    private final Random random;

    private volatile boolean isRunning;
    private volatile Response response;
    private volatile long downloadSize;

    private final DownloadThread downloadThread;
    private RangeInfo rangeInfo;

    private JLogUtils log;

    private boolean isSuc;
    // 是否需要错误异常，正常关闭，不需要，异常关闭，则需要
    private boolean isNeedErrorMsg;

    private String msg;
    private String tip;

    private String taskId;

    RangeThread(int taskId,
                DownloadThread downloadThread,
                RangeInfo rangeInfo) {
        this.random = new Random();
        this.downloadThread = downloadThread;
        this.rangeInfo = rangeInfo;
        this.downloadSize = 0;
        this.log = JLogUtils.getDefault();
        this.isSuc = true;
        this.msg = null;
        this.isNeedErrorMsg = true;
        this.taskId = taskId + "-" + random.nextInt(BOUND);
        this.isRunning = true;

        log.title("Range thread task:" + this.taskId);
    }

    @Override
    public void run() {

        while (rangeInfo != null) {

            if (!isRunning) {
                log.showWarn();
                break;
            }

            File folder = DownloadFileUtils.getFile(rangeInfo.getFolderName());

            boolean exist = DownloadFileUtils.isExist(rangeInfo.getRangeFileName());
            File rangeFile;
            if (exist) {
                rangeFile = DownloadFileUtils.getFile(rangeInfo.getRangeFileName());
            } else {
                rangeFile = FileUtils.createFile(folder, rangeInfo.getRangeFileName());
            }

            if (rangeFile == null) {
                error("文件异常", "RangeFile is null.");
                break;
            }

            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put(ReqHead.RANGE, rangeInfo.getReqRange());

            response = NetUtils.request(rangeInfo.getUrl(), headerMap);

            if (response == null) {
                error("服务器异常", "Response is null.");
                break;
            }

            if (response.code() != 206) {
                error("服务器异常",
                        rangeInfo.getReqRange() + " [Range Thread] Response code is " + response.code());
                break;
            }

            ResponseBody body = response.body();
            if (body == null) {
                error("服务器异常", "Body is null.");
                break;
            }

            OutputStream outputStream;

            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(rangeFile, true));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                error("文件异常", "文件流打开失败 " + e.getMessage());
                break;
            }

            boolean isSucTransmit = transmitStream(body.byteStream(), outputStream);

            if (response != null) {
                response.close();
            }

            if (!isSucTransmit) {
                break;
            }

            // 认领新的任务
            rangeInfo = downloadThread.getRangeTask(downloadSize);
            downloadSize = 0;
        }

        downloadThread.onRangeTaskOver(
                this,
                downloadThread,
                isSuc,
                rangeInfo,
                msg,
                tip);

    }

    /**
     * 转接流，将 输入流 ==转给==> 输出流
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @return 转送成功则返回true，否则返回false
     */
    private boolean transmitStream(InputStream inputStream,
                                   OutputStream outputStream) {

        try {
            byte[] tempByte = new byte[2048];

            while (isRunning) {
                int len = inputStream.read(tempByte);
                if (len == -1) {
                    outputStream.flush();
                    break;
                }

                outputStream.write(tempByte, 0, len);

                synchronized (this) {
                    downloadSize += len;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            error("服务器异常", "Save file error." + e.getMessage());
            return false;
        } finally {
            CloseUtils.close(inputStream);
            CloseUtils.close(outputStream);
        }

        return true;
    }

    /**
     * 错误
     *
     * @param msg 错误信息
     * @param tip 提示
     */
    private void error(String msg, String tip) {
        if (!this.isNeedErrorMsg) {
            return;
        }

        this.isSuc = false;
        this.msg = msg;
        this.tip = tip;
    }

    /**
     * 是否在运行
     */
    public synchronized boolean isRunning() {
        return isRunning;
    }

    /**
     * 暂停
     *
     * @param log 日志
     */
    public synchronized void stop(JLogUtils log) {
        log.content(toString());
        if (!isRunning) {
            return;
        }

        isRunning = false;
        isNeedErrorMsg = false;
    }

    /**
     * 获取下载的进度
     *
     * @return 下载量
     */
    public synchronized long getDownloadSize() {
        long tmp = downloadSize;
        downloadSize = 0;
        return tmp;
    }

    @Override
    public String toString() {
        return "RangeThread{" +
                "random=" + random +
                ", isRunning=" + isRunning +
                ", response=" + response +
                ", downloadSize=" + downloadSize +
                ", downloadThread=" + downloadThread +
                ", rangeInfo=" + rangeInfo +
                ", log=" + log +
                ", isSuc=" + isSuc +
                ", isNeedErrorMsg=" + isNeedErrorMsg +
                ", msg='" + msg + '\'' +
                ", tip='" + tip + '\'' +
                ", taskId='" + taskId + '\'' +
                '}';
    }
}
