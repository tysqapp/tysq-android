package com.abc.lib_multi_download.thread;

import com.abc.lib_multi_download.model.RangeInfo;
import com.abc.lib_log.JLogUtils;

/**
 * author       : frog
 * time         : 2019-11-28 17:09
 * desc         :
 * version      :
 */
public interface IDownloadThread {

    /**
     * 获取分片任务
     *
     * @return 分片任务
     */
    RangeInfo getRangeTask(long rangeDownloadSize);

    /**
     * 存放分片任务
     *
     * @param rangeInfo 分片任务
     */
    void putRangeTask(RangeInfo rangeInfo);

    /**
     * 分片线程结束回调
     *
     * @param rangeThread    分片线程
     * @param downloadThread 下载线程
     * @param isSuc          是否正常结束
     * @param rangeInfo      分片信息·
     * @param msg            异常信息
     * @param tip            异常提示
     */
    void onRangeTaskOver(IRangeThread rangeThread,
                         DownloadThread downloadThread,
                         boolean isSuc,
                         RangeInfo rangeInfo,
                         String msg, String tip);

    /**
     * 停止
     */
    void stop(JLogUtils log);

}
