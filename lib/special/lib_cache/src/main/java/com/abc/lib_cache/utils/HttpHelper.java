package com.abc.lib_cache.utils;

import okhttp3.Protocol;
import okhttp3.Response;

/**
 * author       : frog
 * time         : 2019-09-23 14:41
 * email        : xxxxx
 * desc         : http 协助
 * version      : 1.0.0
 */

public class HttpHelper {

    public static String getStatus(Response response) {
        if (response == null) {
            return "";
        }

        Protocol protocol = response.protocol();
        String protocolString;
        switch (protocol) {
            case HTTP_2:
                protocolString = "HTTP/2";
                break;
            case HTTP_1_1:
                protocolString = "HTTP/1.1";
                break;
            default:
                protocolString = "HTTP/1.1";
                break;
        }
        int code = response.code();
        String message = response.message();

        return protocolString + " " + code + " " + message;
    }

}
