package com.abc.lib_multi_download.okhttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * author       : frog
 * time         : 2019/4/12 上午9:48
 * desc         : 获取OkHttp实例
 * version      : 1.3.0
 */
public class DownloadOkHttpHelper {

    private volatile static OkHttpClient OK_HTTP_CLIENT;

    private static final Object LOCK = new Object();

    public static void setOkHttpClient(OkHttpClient okHttpClient) {
        synchronized (LOCK) {
            OK_HTTP_CLIENT = okHttpClient;
        }
    }

    public static OkHttpClient getOkHttpInstance() {

        if (OK_HTTP_CLIENT == null) {
            synchronized (LOCK) {
                if (OK_HTTP_CLIENT == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    OK_HTTP_CLIENT = builder
                            .connectTimeout(DownloadOkHttpConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(DownloadOkHttpConfig.READ_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(DownloadOkHttpConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .build();

                }
            }
        }

        return OK_HTTP_CLIENT;
    }

}
