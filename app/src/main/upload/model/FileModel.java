package model;

import java.util.ArrayList;

import listener.UploadItemListener;

/**
 * author       : frog
 * time         : 2019/6/13 上午10:16
 * desc         : 文件信息
 * version      : 1.3.0
 */
public class FileModel {

    // 锁，获取资源都需要使用该锁
    public final Object LOCK;

    // 文件名
    private final String filename;
    // 文件路径
    private final String url;
    // 文件大小
    private final long totalSize;
    // 已经上传的分片下标
    private final ArrayList<Integer> uploaded;
    // 正在上传的分片下标
    private final ArrayList<Integer> uploading;
    // hash 值
    private String hash;
    // 总共分片数
    private int sliceCount;
    // 分片的大小
    private long sliceLength;
    // 是否错误
    private boolean isError;
    // 错误信息
    private String errorMsg;
    // 创建事件
    private final String createTime;

    // 监听器
    private UploadItemListener mListener;

    // 进度
    private int percent;

    // 文件类型
    private final int fileType;

    public FileModel(String filename,
                     String url,
                     long totalSize,
                     String createTime,
                     int fileType) {

        this.LOCK = new Object();

        this.filename = filename;
        this.url = url;
        this.totalSize = totalSize;
        this.createTime = createTime;

        this.uploaded = new ArrayList<>();
        this.uploading = new ArrayList<>();

        this.mListener = null;

        this.isError = false;

        this.percent = 0;

        this.fileType = fileType;

    }

    public String getFilename() {
        return filename;
    }

    public String getUrl() {
        return url;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public ArrayList<Integer> getUploaded() {
        return uploaded;
    }

    public ArrayList<Integer> getUploading() {
        return uploading;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getSliceCount() {
        return sliceCount;
    }

    public void setSliceCount(int sliceCount) {
        this.sliceCount = sliceCount;
    }

    public long getSliceLength() {
        return sliceLength;
    }

    public void setSliceLength(long sliceLength) {
        this.sliceLength = sliceLength;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void removeListener() {
        this.mListener = null;
    }

    public UploadItemListener getListener() {
        return mListener;
    }

    public void setListener(UploadItemListener listener) {
        this.mListener = listener;
    }

    public String getCreateTime() {
        return createTime;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getFileType() {
        return fileType;
    }
}
