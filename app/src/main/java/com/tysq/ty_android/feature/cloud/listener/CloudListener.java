package com.tysq.ty_android.feature.cloud.listener;

/**
 * author       : frog
 * time         : 2019/6/5 下午2:27
 * desc         : 云盘监听器
 * version      : 1.3.0
 */
public interface CloudListener {

    void setUploadedInfo(int count);

    void setUploadingInfo(int count);

    void setDownloadInfo(int count);

}
