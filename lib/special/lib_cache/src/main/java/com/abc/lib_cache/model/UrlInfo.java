package com.abc.lib_cache.model;

import android.net.Uri;

import com.abc.lib_log.JLogUtils;

/**
 * author       : frog
 * time         : 2019-09-24 14:21
 * desc         : url 信息
 * version      : 1.0.0
 */
public class UrlInfo {

    // 请求路径
    private final String url;
    private final Uri uri;

    // 资源文件夹
    private String folderName;
    // 码率
    private String rateCode;
    // 资源文件
    private String fileName;

    // 类型
    private String type;

    // 是否解析成功
    private boolean isSuc;

    public UrlInfo(String url) {
        this.url = url;
        this.uri = Uri.parse(url);
    }

    public Uri getUri() {
        return uri;
    }

    public String getUrl() {
        return url;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getRateCode() {
        return rateCode;
    }

    public void setRateCode(String rateCode) {
        this.rateCode = rateCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isSuc() {
        return isSuc;
    }

    public void setSuc(boolean suc) {
        isSuc = suc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void show(JLogUtils log) {
        log.param("Url: " + getUrl())
                .param("FileName: " + getFileName())
                .param("FolderName:" + getFolderName())
                .param("RateCode: " + getRateCode())
                .param("Type: " + getType())
                .param("isSuc: " + isSuc())
                .enterContent();
    }

}
