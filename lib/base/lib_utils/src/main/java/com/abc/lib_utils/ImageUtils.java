package com.abc.lib_utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;

/**
 * author       : frog
 * time         : 2019-09-24 11:05
 * email        : xxxxx
 * desc         : 图像工具
 * version      : 1.0.0
 */

public class ImageUtils {

    private static final BitmapFactory.Options OPTIONS = new BitmapFactory.Options();

    static {
        OPTIONS.inJustDecodeBounds = true;
    }

    /**
     * 向系统发起一个扫描文件的请求
     *
     * @param context  上下文
     * @param filePath 文件路径
     */
    public static void updateImageInSystem(Context context, String filePath) {
        updateImageInSystem(context, new File(filePath));
    }

    /**
     * 向系统发起一个扫描文件的请求
     *
     * @param context 上下文
     * @param file    文件
     */
    public static void updateImageInSystem(Context context, File file) {
        context.sendBroadcast(
                new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(file)));
    }

    /**
     * 获取 mimeType
     *
     * @param file 文件
     * @return 正常会返回：”image/png”、”image/jpeg”、”image/gif”；否则会返回null
     */
    public static String getMimeType(File file) {
        String filePath = file.getPath();
        BitmapFactory.decodeFile(filePath, OPTIONS);
        return OPTIONS.outMimeType;
    }

}
