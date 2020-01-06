package com.abc.lib_multi_download.jerry;

import com.abc.lib_multi_download.constants.DownloadConstants;
import com.abc.lib_multi_download.download.task.AddTask;
import com.abc.lib_multi_download.download.task.DeleteTask;
import com.abc.lib_multi_download.download.task.PauseTask;
import com.abc.lib_multi_download.download.task.RemoveTask;
import com.abc.lib_multi_download.download.task.SearchTask;
import com.abc.lib_multi_download.model.DownloadInfo;
import com.abc.lib_multi_download.model.status.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * author       : frog
 * time         : 2019-11-29 17:24
 * desc         : 多线程下载入口
 * version      : 1.0.0
 */
public class JerryDownload {

    private static volatile JerryDownload instance = null;

    // 任务线程池
    private final ExecutorService taskPool;

    private JerryDownload() {
        taskPool = new ThreadPoolExecutor(0, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
    }

    public static JerryDownload getInstance() {

        if (instance == null) {
            synchronized (JerryDownload.class) {
                if (instance == null) {
                    instance = new JerryDownload();
                }
            }
        }

        return instance;

    }

    /**
     * 下载
     *
     * @param url      下载地址
     * @param filename 文件名称
     * @param cover    封面
     */
    public void download(String url,
                         String filename,
                         String cover) {
        download(url,
                filename,
                DownloadConstants.DEFAULT_USER_ID,
                DownloadConstants.DEFAULT_DOMAIN,
                cover);
    }

    /**
     * 下载
     *
     * @param url      下载地址
     * @param filename 文件名称
     * @param userId   用户id
     * @param cover    封面
     */
    public void download(String url,
                         String filename,
                         int userId,
                         String cover) {
        download(url,
                filename,
                userId,
                DownloadConstants.DEFAULT_DOMAIN,
                cover);
    }

    /**
     * 下载
     *
     * @param url      下载地址
     * @param filename 文件名称
     * @param userId   用户id
     * @param domain   域名
     * @param cover    封面
     */
    public void download(String url,
                         String filename,
                         int userId,
                         String domain,
                         String cover) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setUserId(userId);
        downloadInfo.setDomain(domain);
        downloadInfo.setUrl(url);
        downloadInfo.setFileName(filename);
        downloadInfo.setCreateTime(System.currentTimeMillis());
        downloadInfo.setStatus(Status.INIT);
        downloadInfo.setCover(cover);
        download(downloadInfo);
    }

    /**
     * 下载
     *
     * @param downloadInfo 下载信息
     */
    public void download(DownloadInfo downloadInfo) {
        AddTask addTask = new AddTask(downloadInfo);
        taskPool.submit(addTask);
    }

    /**
     * 暂停
     *
     * @param downloadInfo 下载信息
     */
    public void pause(DownloadInfo downloadInfo) {
        PauseTask stopTask = new PauseTask(downloadInfo);
        taskPool.submit(stopTask);
    }

    /**
     * 删除
     *
     * @param downloadInfo 下载信息
     */
    public void delete(DownloadInfo downloadInfo) {
        DeleteTask deleteTask = new DeleteTask(downloadInfo);
        taskPool.submit(deleteTask);
    }

    /**
     * 移除下载任务，内部使用
     *
     * @param downloadInfo 下载信息
     */
    public void _remove(DownloadInfo downloadInfo) {
        RemoveTask removeTask = new RemoveTask(downloadInfo);
        taskPool.submit(removeTask);
    }

    /**
     * 获取下载信息列表
     *
     * @return 下载信息列表
     */
    public List<DownloadInfo> getDownloadList() {
        return getDownloadList(DownloadConstants.DEFAULT_USER_ID, DownloadConstants.DEFAULT_DOMAIN);
    }

    /**
     * 获取下载信息列表
     *
     * @param userId 用户Id
     * @return 下载信息列表
     */
    public List<DownloadInfo> getDownloadList(int userId) {
        return getDownloadList(userId, DownloadConstants.DEFAULT_DOMAIN);
    }

    /**
     * 获取下载信息列表
     *
     * @param userId 用户Id
     * @param domain 域名
     * @return 下载信息列表
     */
    public List<DownloadInfo> getDownloadList(int userId, String domain) {
        SearchTask searchTask = new SearchTask(userId, domain);

        Future<List<DownloadInfo>> submit = taskPool.submit(searchTask);

        try {
            return submit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}
