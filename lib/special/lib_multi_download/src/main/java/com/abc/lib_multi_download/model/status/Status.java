package com.abc.lib_multi_download.model.status;

public interface Status {

    // 未进行初始化
    int INIT = 1;

    // 已经初始化了
    int DOWNLOAD = 1 << 1;

    // 成功（下载完成）
    int SUCCESS = 1 << 2;

    // 异常 【异常能和其他的状态并存】
    int EXCEPTION = 1 << 3;

}