package com.tysq.ty_android.net;

import com.bit.net.LogInterceptor;
import com.tysq.ty_android.net.config.NetConfig;
import com.tysq.ty_android.net.cookie.CookieJarImpl;
import com.tysq.ty_android.net.cookie.PersistentCookieStore;
import com.tysq.ty_android.net.interceptor.CommonParamsInterceptor;
import com.abc.lib_multi_download.download.JerryDownloadConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
                            .connectTimeout(NetConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(NetConfig.READ_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(NetConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .addInterceptor(new CommonParamsInterceptor())
                            .addInterceptor(new LogInterceptor())
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request()
                                            .newBuilder()
                                            .removeHeader("User-Agent")
                                            .addHeader("User-Agent", "android")
                                            .build();
                                    return chain.proceed(request);
                                }
                            })
                            .cookieJar(new CookieJarImpl(PersistentCookieStore.getInstance()))
                            .build();

                    JerryDownloadConfig.setOkHttp(OK_HTTP_CLIENT);

                }
            }
        }

        return OK_HTTP_CLIENT;
    }

}
