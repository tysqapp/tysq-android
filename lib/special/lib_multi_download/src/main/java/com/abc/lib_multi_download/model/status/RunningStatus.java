package com.abc.lib_multi_download.model.status;

/**
 * author       : frog
 * time         : 2019-11-29 15:33
 * email        : xxxxx
 * desc         : 运行中的状态
 * version      : 1.0.0
 */

public interface RunningStatus {

    // 停止
    int PAUSE = 0;

    // 等待下载
    int WAITING = 1;

    // 初始化
    int INIT = 2;

    // 开始下载
    int DOWNLOADING = 3;

    // 成功（下载完成）
    int SUCCESS = 4;

    // 异常
    int EXCEPTION = 5;

    // 进度
    int PROGRESS = 6;

    // 删除
    int DELETE = 7;

}