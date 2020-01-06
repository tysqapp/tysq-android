package com.abc.lib_multi_download.download;

import android.content.Context;

import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.JerryMultiDownloadGeneratedDatabaseHolder;
import com.abc.lib_multi_download.config.DownloadConfig;
import com.abc.lib_multi_download.model.DownloadDB;
import com.abc.lib_multi_download.okhttp.DownloadOkHttpHelper;

import okhttp3.OkHttpClient;

/**
 * author       : frog
 * time         : 2019-10-23 15:29
 * desc         : 下载入口
 * version      : 1.0.0
 */
public class JerryDownloadConfig {

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        FlowManager
                .init(FlowConfig.builder(context)
                        .addDatabaseConfig(
                                DatabaseConfig.builder(DownloadDB.class)
                                        .databaseName(DownloadDB.NAME)
                                        .build()
                        )
                        .build());

        FlowManager.initModule(JerryMultiDownloadGeneratedDatabaseHolder.class);
    }

    /**
     * okhttp 设置
     *
     * @param okHttpClient okhttp
     */
    public static void setOkHttp(OkHttpClient okHttpClient) {
        DownloadOkHttpHelper.setOkHttpClient(okHttpClient);
    }

    /**
     * 设置下载的线程数
     *
     * @param count 线程数
     */
    public static void setThreadCount(int count) {
        DownloadConfig.getInstance().setThreadCount(count);
    }

    /**
     * 设置下载的路径
     *
     * @param path 下载路径
     */
    public static void setDownloadFolder(String path) {
        DownloadConfig.getInstance().setDownloadFile(path);
    }

}
