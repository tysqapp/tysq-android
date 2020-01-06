package com.abc.lib_multi_download.model;

import com.abc.lib_log.JLogUtils;
import com.abc.lib_multi_download.constants.DownloadConstants;
import com.abc.lib_multi_download.listener.DownloadListener;
import com.abc.lib_multi_download.model.status.RunningStatus;
import com.abc.lib_multi_download.model.status.Status;
import com.abc.lib_multi_download.utils.BitUtils;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.File;

/**
 * author       : frog
 * time         : 2019-11-27 14:14
 * desc         : 下载信息
 * version      : 1.0.0
 */

@Table(database = DownloadDB.class)
public class DownloadInfo extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long id;

    /**
     * 用户id 【必填】
     */
    @Column(defaultValue = DownloadConstants.DEFAULT_USER_ID + "")
    private int userId;

    /**
     * 域名
     */
    @Column
    private String domain;

    /**
     * 封面
     */
    @Column
    private String cover;

    // 路径
    @Column
    private String url;

    // 最终文件名字
    @Column
    private String fileName;

    // 合并分片的文件名
    @Column
    private String tmpFileName;

    // 存放range文件的名称
    @Column
    private String folderName;

    // 状态
    @Column
    private int status;

    // 创建时间
    @Column
    private long createTime;

    // 错误信息
    @Column
    private String msg;

    // 文件大小
    @Column(defaultValue = "-1")
    private long totalSize;

    // 分片大小
    @Column
    private long rangeSize;

    // 最后更改时间
    @Column
    private String lastModified;

    // 类型
    @Column
    private String mimeType;

    // 类型
    @Column
    private String subType;

    // 已经下载的大小
    @Column
    private long downloadSize;

    // 进度
    @Column
    private long progress;

    // 回调
    private volatile DownloadListener mListener;

    // 分片存放文件夹
    private File rangeFolder;

    // 运行中的状态
    private int runningStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getRangeSize() {
        return rangeSize;
    }

    public void setRangeSize(long rangeSize) {
        this.rangeSize = rangeSize;
    }

    public synchronized DownloadListener getListener() {
        return mListener;
    }

    public synchronized void setListener(DownloadListener listener) {
        if (this.mListener != null) {
            removeListener();
        }

        this.mListener = listener;
    }

    public synchronized void removeListener() {
        if (this.mListener == null) {
            return;
        }
        this.mListener = null;
    }

    public File getRangeFolder() {
        return rangeFolder;
    }

    public void setRangeFolder(File rangeFolder) {
        this.rangeFolder = rangeFolder;
    }

    public String getTmpFileName() {
        return tmpFileName;
    }

    public void setTmpFileName(String tmpFileName) {
        this.tmpFileName = tmpFileName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(int runningStatus) {
        this.runningStatus = runningStatus;
    }

    public long getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public boolean isPause() {
        return runningStatus == RunningStatus.PAUSE || runningStatus == RunningStatus.EXCEPTION;
    }

    public boolean isSuccess() {
        return runningStatus == RunningStatus.SUCCESS;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public void log(JLogUtils log) {
        log.title("DownloadInfo")
                .param("id=" + id)
                .param("userId=" + userId)
                .param("domain=" + domain)
                .param("url=" + url)
                .param("cover=" + cover)
                .param("fileName=" + fileName)
                .param("tmpFileName=" + tmpFileName)
                .param("folderName=" + folderName)
                .param("status=" + getStatus(status))
                .param("createTime=" + createTime)
                .param("msg=" + msg)
                .param("totalSize=" + totalSize)
                .param("rangeSize=" + rangeSize)
                .param("lastModified=" + lastModified)
                .param("mimeType=" + mimeType)
                .param("mListener=" + mListener)
                .param("rangeFolder=" + rangeFolder)
                .param("progress=" + progress)
                .param("runningStatus=" + getRunningStatus(runningStatus));
    }

    public static String getStatus(int type) {
        String result = "";

        if (BitUtils.isContain(type, Status.INIT)) {
            result += "INIT | ";
        }

        if (BitUtils.isContain(type, Status.DOWNLOAD)) {
            result += "DOWNLOAD | ";
        }

        if (BitUtils.isContain(type, Status.SUCCESS)) {
            result += "SUCCESS | ";
        }

        if (BitUtils.isContain(type, Status.EXCEPTION)) {
            result += "EXCEPTION | ";
        }

        return type + result;
    }

    public static String getRunningStatus(int type) {
        switch (type) {
            case RunningStatus.PAUSE:
                return "PAUSE";
            case RunningStatus.WAITING:
                return "WAITING";
            case RunningStatus.INIT:
                return "INIT";
            case RunningStatus.DOWNLOADING:
                return "DOWNLOADING";
            case RunningStatus.SUCCESS:
                return "SUCCESS";
            case RunningStatus.EXCEPTION:
                return "EXCEPTION";
            default:
                return "UNKNOW【" + type + "】";

        }
    }

}
