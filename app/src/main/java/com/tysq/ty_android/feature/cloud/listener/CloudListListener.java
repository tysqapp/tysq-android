package com.tysq.ty_android.feature.cloud.listener;

/**
 * author       : frog
 * time         : 2019/6/5 下午6:23
 * desc         :
 * version      : 1.3.0
 */
public interface CloudListListener {

    void deleteFile(int fileId);

    void chooseCover(int id);

    void downloadFile(int id,
                      String filename,
                      int accountId,
                      boolean isVideo,
                      String downloadUrl,
                      String cover);

    void rename(int id);
}
