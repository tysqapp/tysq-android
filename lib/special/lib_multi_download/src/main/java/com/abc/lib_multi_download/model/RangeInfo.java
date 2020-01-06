package com.abc.lib_multi_download.model;

/**
 * author       : frog
 * time         : 2019-11-27 14:14
 * desc         : 分片信息
 * version      : 1.0.0
 */
public class RangeInfo {

    // 请求起始下标
    private long start;

    // 请求终止下标
    private long end;

    // 真正的起始下标
    private long realStart;

    // 请求分片报文
    private String reqRange;

    // 存储文件名称
    private String rangeFileName;

    // 文件夹名称
    private String folderName;

    private String url;

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getReqRange() {
        return reqRange;
    }

    public void setReqRange(String reqRange) {
        this.reqRange = reqRange;
    }

    public String getRangeFileName() {
        return rangeFileName;
    }

    public void setRangeFileName(String rangeFileName) {
        this.rangeFileName = rangeFileName;
    }

    public long getRealStart() {
        return realStart;
    }

    public void setRealStart(long realStart) {
        this.realStart = realStart;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "RangeInfo{" +
                "start=" + start +
                ", end=" + end +
                ", realStart=" + realStart +
                ", reqRange='" + reqRange + '\'' +
                ", rangeFileName='" + rangeFileName + '\'' +
                ", folderName='" + folderName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
