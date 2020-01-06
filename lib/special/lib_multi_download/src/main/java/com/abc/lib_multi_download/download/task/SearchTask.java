package com.abc.lib_multi_download.download.task;

import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.abc.lib_multi_download.constants.DownloadConstants;
import com.abc.lib_multi_download.model.DownloadInfo;
import com.abc.lib_multi_download.model.DownloadInfo_Table;
import com.abc.lib_multi_download.model.status.RunningStatus;
import com.abc.lib_multi_download.model.status.Status;
import com.abc.lib_multi_download.thread.IDownloadThread;
import com.abc.lib_multi_download.utils.BitUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * author       : frog
 * time         : 2019-11-29 10:48
 * desc         : 查询下载任务
 * version      : 1.0.0
 */
public class SearchTask extends BaseTask implements Callable<List<DownloadInfo>> {

    private int userId;
    private String domain;

    public SearchTask(int userId,
                      String domain) {
        super(null);
        this.userId = userId;
        this.domain = domain;
    }

    @Override
    public List<DownloadInfo> call() throws Exception {

        synchronized (LOCK) {

            log.title("Search task NO:" + taskId)
                    .param("userId: " + userId)
                    .param("domain: " + domain);

            OrderBy orderBy = OrderBy
                    .fromProperty(DownloadInfo_Table.createTime)
                    .descending();

            List<DownloadInfo> downloadInfoList;

            if (userId == DownloadConstants.DEFAULT_USER_ID
                    && domain.equals(DownloadConstants.DEFAULT_DOMAIN)) {
                // userId 和 域名 均为默认值
                downloadInfoList = SQLite.select()
                        .from(DownloadInfo.class)
                        .orderBy(orderBy)
                        .queryList();

            } else if (userId != DownloadConstants.DEFAULT_USER_ID
                    && domain.equals(DownloadConstants.DEFAULT_DOMAIN)) {

                // userId 不为默认值， 域名 均为默认值
                downloadInfoList = SQLite.select()
                        .from(DownloadInfo.class)
                        .where(DownloadInfo_Table.userId.eq(userId))
                        .orderBy(orderBy)
                        .queryList();

            } else if (userId == DownloadConstants.DEFAULT_USER_ID
                    && !domain.equals(DownloadConstants.DEFAULT_DOMAIN)) {

                // userId 为默认值、 域名 不为默认值
                downloadInfoList = SQLite.select()
                        .from(DownloadInfo.class)
                        .where(DownloadInfo_Table.domain.eq(domain))
                        .orderBy(orderBy)
                        .queryList();

            } else {

                // userId 和 域名 均不为默认值
                downloadInfoList = SQLite.select()
                        .from(DownloadInfo.class)
                        .where(DownloadInfo_Table.userId.eq(userId),
                                DownloadInfo_Table.domain.eq(domain))
                        .orderBy(orderBy)
                        .queryList();

            }


            // 按照 Status 填充 运行时curStatus 的值
            for (int i = 0; i < downloadInfoList.size(); i++) {
                DownloadInfo downloadInfo = downloadInfoList.get(i);

                // 异常状态
                if (BitUtils.isContain(downloadInfo.getStatus(), Status.EXCEPTION)) {
                    downloadInfo.setRunningStatus(RunningStatus.EXCEPTION);
                    continue;
                }

                // 完成状态
                if (BitUtils.isContain(downloadInfo.getStatus(), Status.SUCCESS)) {
                    downloadInfo.setRunningStatus(RunningStatus.SUCCESS);
                    continue;
                }

                // 是否存在运行的线程中
                boolean isExist = false;

                // 将从 数据库中查找 的对象替换成 线程池中的对象
                for (Map.Entry<DownloadInfo, IDownloadThread> entry
                        : DOWNLOAD_POOL.getDownloadInfoMap().entrySet()) {

                    DownloadInfo key = entry.getKey();

                    if (key.getId() == downloadInfo.getId()) {  // 在线程池中

                        downloadInfoList.remove(downloadInfo);
                        downloadInfoList.add(i, key);

                        isExist = true;
                        break;
                    }

                }

                // 不存在，则添加为暂停
                if (!isExist) {
                    downloadInfo.setRunningStatus(RunningStatus.PAUSE);
                }

            }

            log.content("downloadInfoList size: " + downloadInfoList.size())
                    .showInfo();

            return downloadInfoList;
        }
    }

}
