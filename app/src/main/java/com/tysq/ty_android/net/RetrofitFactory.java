package com.tysq.ty_android.net;

import com.tysq.ty_android.net.api.ApiService;
import com.tysq.ty_android.net.config.NetConfig;

/**
 * author       : frog
 * time         : 2018-06-06 16:02
 * desc         : 创建retrofit
 * version      : 1.3.0
 */

public class RetrofitFactory {

    private static final Object LOCK = new Object();

    private static volatile ApiService API_SERVICE;

    public static ApiService getApiService() {

        if (API_SERVICE == null) {
            synchronized (LOCK) {
                if (API_SERVICE == null) {
                    String domain = NetConfig.getBaseUrl();

                    API_SERVICE =
                            new RetrofitHelper(domain, OkHttpHelper.getOkHttpInstance())
                                    .create(ApiService.class);
                }
            }
        }
        return API_SERVICE;

    }

    public static ApiService getApiService(String domain) {

        if (API_SERVICE == null) {
            synchronized (LOCK) {
                if (API_SERVICE == null) {
                    domain = NetConfig.getBaseUrl(domain);

                    API_SERVICE =
                            new RetrofitHelper(domain, OkHttpHelper.getOkHttpInstance())
                                    .create(ApiService.class);
                }
            }
        }
        return API_SERVICE;

    }

    public static void clear() {
        API_SERVICE = null;
    }

}
