package com.tysq.ty_android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.abc.lib_utils.CloseUtils;
import com.abc.lib_utils.EncryptionUtils;
import com.abc.lib_utils.FileUtils;
import com.abc.lib_utils.ImageUtils;
import com.tysq.ty_android.config.TyConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * author       : frog
 * time         : 2019-10-30 14:39
 * desc         : 文件工具
 * version      : 5.1.0
 */
public class TyFileUtils {

    /**
     * 创建文件
     *
     * @param folder   文件夹
     * @param fileName 文件名称
     * @return 文件
     */
    public static File createFile(File folder, String fileName) {
        return FileUtils.createFile(folder, fileName);
    }

    /**
     * 获取文件名
     *
     * @param content 文件
     * @return md5的内容
     */
    public static String getFileName(String content) {
        return EncryptionUtils.md5(content);
    }

    private static File createSaveFolder(String url, String folder) {
        return FileUtils.createFolder(new File(url), folder);
    }

    /**
     * 获取二维码存储的文件夹
     */
    public static File getQrFolder() {
        return getSaveFolder(TyConfig.Folder.QR);
    }

    /**
     * 获取图片存储的文件夹
     */
    public static File getImgFolder() {
        return getSaveFolder(TyConfig.Folder.IMG);
    }

    /**
     * 获取云盘下载存储的文件夹
     */
    public static File getCloudDownloadFolder() {
        return getSaveFolder(TyConfig.Folder.CLOUD_DOWNLOAD);
    }

    /**
     * 获取主存储文件夹
     */
    public static File getMainFolder() {
        return getSaveFolder(TyConfig.Folder.MAIN);
    }

    /**
     * 获取保存的文件夹
     *
     * @return
     */
    public static File getSaveFolder(String folder) {
        String path;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            path = Environment.getDataDirectory().getAbsolutePath();
        }

        return createSaveFolder(path, folder);
    }

    /**
     * 保存图片
     *
     * @param bitmap
     * @return
     */
    public static String saveBitmap(Bitmap bitmap) {
        File folder = getImgFolder();
        if (folder == null) {
            return "";
        }

        File filePic;

        FileOutputStream fos = null;

        try {

            filePic = new File(folder, (System.currentTimeMillis() / 1000) + ".jpg");

            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
            }

            fos = new FileOutputStream(filePic);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);

            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            CloseUtils.close(fos);
        }

        return filePic.getAbsolutePath();
    }

    /**
     * 向系统发起一个扫描文件的请求
     *
     * @param context 上下文
     * @param file    文件
     */
    public static void updateImageInSystem(Context context, File file) {
        ImageUtils.updateImageInSystem(context, file);
    }

    /**
     * 保存文件
     *
     * @param file        需要保存的文件
     * @param inputStream 输入流
     */
    public static void saveToFile(File file, InputStream inputStream) {
        FileUtils.saveStreamToFile(file, inputStream);
    }

    /**
     * 删除文件
     *
     * @param file 需要删除的文件
     */
    public static void deleteFile(File file) {
        FileUtils.deleteFile(file);
    }
}
