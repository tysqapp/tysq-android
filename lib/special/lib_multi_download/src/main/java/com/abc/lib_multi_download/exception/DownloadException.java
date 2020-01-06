package com.abc.lib_multi_download.exception;

/**
 * author       : frog
 * time         : 2019-11-27 15:43
 * desc         : 下载过程异常
 * version      : 1.0.0
 */
public class DownloadException extends Exception {

    private String tip;

    public DownloadException(String message, String tip) {
        super(message);
        this.tip = tip;
    }

    public String getTip() {
        return tip;
    }
}
