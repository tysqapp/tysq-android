package com.abc.lib_cache.utils;

import android.net.Uri;
import android.text.TextUtils;

import com.abc.lib_utils.EncryptionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * author       : frog
 * time         : 2019-10-29 11:05
 * desc         : 域名信息
 * version      : 5.1.0
 */
public class HostInfo {

    private static final Map<String, String> HOST_MAP = new HashMap<>();

    public static void addHost(String key, String host) {
        synchronized (HOST_MAP) {
            HOST_MAP.put(key, host);
        }
    }

    public static String getHost(String key) {
        synchronized (HOST_MAP) {
            return HOST_MAP.get(key);
        }
    }

    /**
     * 获取key
     */
    public static String getKey(Uri uri) {

        String host = uri.getHost();

        if (host == null) {
            return uri.toString();
        }

        String url = uri.toString();

        // 获取 域名 的下标
        int hostIndex = url.indexOf(host);
        // url的后缀  /api/file/commons/990/[文件]/1080p00000.ts
        String urlSuffix = url.substring(hostIndex + host.length());

        // 最后一个片段 1080p00000.ts
        String lastPathSegment = uri.getLastPathSegment();
        String filePath;
        if (!TextUtils.isEmpty(lastPathSegment)) {
            filePath = urlSuffix.replace(lastPathSegment, "");
        } else {
            filePath = urlSuffix;
        }

        return EncryptionUtils.md5(filePath);

    }

}
