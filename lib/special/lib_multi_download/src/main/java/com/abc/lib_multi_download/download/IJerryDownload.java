package com.abc.lib_multi_download.download;

import com.abc.lib_multi_download.model.DownloadInfo;

import java.util.List;

/**
 * author       : frog
 * time         : 2019-10-23 15:09
 * desc         : 下载接口
 * version      : 1.0.0
 */
public interface IJerryDownload {

    /**
     * 添加下载
     *
     * @param url      下载路径
     * @param filename 下载文件名
     * @param userId   用户id
     * @return 下载信息
     */
    DownloadInfo download(String url,
                          String filename,
                          int userId,
                          String domain,
                          String cover);

    /**
     * 添加下载
     *
     * @param url      下载路径
     * @param filename 下载文件名
     * @return 下载信息
     */
    DownloadInfo download(String url,
                          String filename);

    /**
     * 添加下载
     *
     * @param downloadInfo 下载信息
     * @return 下载信息
     */
    DownloadInfo download(DownloadInfo downloadInfo);

    /**
     * 停止下载
     */
    void stopTask(DownloadInfo downloadInfo);

    /**
     * 获取下载信息
     *
     * @return 下载信息
     */
    List<DownloadInfo> getDownloadInfo(int userId,
                                       String domain);

    /**
     * 移除下载信息
     *
     * @param downloadInfo 下载信息
     */
    void removeDownloadInfo(DownloadInfo downloadInfo);

    /**
     * 删除
     */
    void delete(DownloadInfo downloadInfo);

}
