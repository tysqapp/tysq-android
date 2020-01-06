package com.abc.lib_cache.okhttp;

import com.abc.lib_cache.okhttp.interceptor.CommonParamsInterceptor;
import com.bit.net.LogInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * author       : frog
 * time         : 2019/4/12 上午9:48
 * desc         : 获取OkHttp实例
 * version      : 1.3.0
 */
public class OkHttpHelper {

    private volatile static OkHttpClient OK_HTTP_CLIENT;

    private static final Object LOCK = new Object();

    public static OkHttpClient getOkHttpInstance() {

        if (OK_HTTP_CLIENT == null) {
            synchronized (LOCK) {
                if (OK_HTTP_CLIENT == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    OK_HTTP_CLIENT = builder
                            .connectTimeout(OkHttpConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(OkHttpConfig.READ_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(OkHttpConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .followRedirects(false)
                            .followSslRedirects(false)
                            .addInterceptor(new CommonParamsInterceptor())
                            .addInterceptor(new LogInterceptor())
                            .build();

                }
            }
        }

        return OK_HTTP_CLIENT;
    }

}
