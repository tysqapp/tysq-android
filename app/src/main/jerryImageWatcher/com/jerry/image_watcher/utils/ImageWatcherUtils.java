package com.jerry.image_watcher.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.config.PictureMimeType;

import java.io.File;

/**
 * author       : frog
 * time         : 2019-09-03 11:56
 * desc         : 检测是否存在
 * version      : 1.4.0
 */
public class ImageWatcherUtils {

    private static final String GLIDE_CACHE_SUFFIX = ".0";

    /**
     * 检测是否在 glide 中存在对应的文件
     *
     * @param url 网络图片资源路径
     * @return true：存在；false：不存在
     */
    public static boolean isExistInGlideCache(Context context, String url) {
        File file = getGlideCacheFileViaUrl(context, url);
        return file != null && file.exists();
    }

    /**
     * 通过 url 获取 glide 的存储文件
     *
     * @param context 上下文
     * @param url     url
     * @return 存储文件
     */
    public static File getGlideCacheFileViaUrl(Context context, String url) {
        File photoCacheDir = Glide.getPhotoCacheDir(context);

        if (photoCacheDir == null) {
            return null;
        }

        String key = EncryptionUtils.sha256Encrypt(url);

        return new File(photoCacheDir, key + GLIDE_CACHE_SUFFIX);
    }

    private static final BitmapFactory.Options OPTIONS = new BitmapFactory.Options();

    static {
        OPTIONS.inJustDecodeBounds = true;
    }

    /**
     * 获取 mimeType
     *
     * @param file 文件
     * @return ”image/png”、”image/jpeg”、”image/gif”
     */
    public static String getMimeType(File file) {
        String filePath = file.getPath();
        BitmapFactory.decodeFile(filePath, OPTIONS);
        String mimeType = OPTIONS.outMimeType;
        Log.i("ImageWatcherUtils", "getMimeType: " + mimeType);
        return mimeType;

    }

}
