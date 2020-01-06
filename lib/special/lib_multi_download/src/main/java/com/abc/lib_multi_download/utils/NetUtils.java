package com.abc.lib_multi_download.utils;

import com.abc.lib_multi_download.okhttp.DownloadOkHttpHelper;

import java.io.IOException;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * author       : frog
 * time         : 2019-10-14 16:20
 * desc         :
 * version      :
 */
public class NetUtils {

    /**
     * 进行请求
     */
    public static Response request(String url,
                                   Map<String, String> headers) {
        // 进行网络请求
        Request.Builder builder = new Request.Builder();
        builder.url(url);

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Response response = null;

        try {
            response = DownloadOkHttpHelper.getOkHttpInstance()
                    .newCall(builder.build())
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}
