package com.abc.lib_multi_download.model;

/**
 * author       : frog
 * time         : 2019-11-29 18:10
 * desc         :
 * version      :
 */
public class DownloadEvent {

    private final long id;
    private final int status;

    public DownloadEvent(long id, int status) {
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }
}
