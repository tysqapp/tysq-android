package com.abc.lib_cache.utils;

import android.net.Uri;
import android.text.TextUtils;

import com.abc.lib_utils.EncryptionUtils;

/**
 * author       : frog
 * time         : 2019-09-26 14:15
 * desc         : url
 * version      : 1.0.0
 */
public class UrlHelper {

    private static final String DOT = ".";

    /**
     * 获取 确定一个文件的路径
     */
    public static String getFolderName(Uri uri) {
        String lastPathSegment = uri.getLastPathSegment();
        String path = uri.getPath();

        if (TextUtils.isEmpty(path)) {
            return null;
        }

        // 将最后的 片段 替换
        if (!TextUtils.isEmpty(lastPathSegment)) {
            path = path.replace(lastPathSegment, "");
        }

        return EncryptionUtils.md5(path);
    }

    /**
     * 获取 不带后缀 的名称
     *
     * @param name xxx.jpg
     * @return xxx
     */
    public static String getFileName(String name) {
        if (TextUtils.isEmpty(name) || !name.contains(DOT)) {
            return name;
        }

        int index = name.lastIndexOf(DOT);

        if (index == -1) {
            return name;
        }

        return name.substring(0, index);
    }

    /**
     * https://xxx.tysqapp.com/api/files/990/e43fcf1d98071ce7380663e3c8acbf7f86fcf78fd0/480p00001.ts
     * ====》
     * MD5("/api/files/990/e43fcf1d98071ce7380663e3c8acbf7f86fcf78fd0/")
     */
    public static String getPathMd5(Uri uri) {
        String path = uri.getPath();
        String lastPathSegment = uri.getLastPathSegment();

        if (path != null && lastPathSegment != null) {
            path = path.replace(lastPathSegment, "");
        }

        if (path == null) {
            return "";
        }

        return EncryptionUtils.md5(path);
    }

}