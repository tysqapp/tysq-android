package com.abc.lib_multi_download.utils;

import android.os.Environment;

import com.abc.lib_multi_download.config.DownloadConfig;
import com.abc.lib_utils.FileUtils;

import java.io.File;

/**
 * author       : frog
 * time         : 2019-10-14 15:13
 * desc         : 下载文件
 * version      : 1.0.0
 */
public class DownloadFileUtils {

    /**
     * 获取文件夹
     *
     * @return 文件夹
     */
    public static File getFolder() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return createSaveFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
        } else {
            return createSaveFolder(Environment.getDataDirectory().getAbsolutePath());
        }
    }

    private static File createSaveFolder(String url) {
        return FileUtils.createFolder(
                new File(url),
                DownloadConfig.getInstance().getDownloadFile());
    }

    /**
     * 获取是否存在
     *
     * @param filename 文件名
     * @return true 已经存在，false 未存在
     */
    public static boolean isExist(String filename) {
        File file = getFile(filename);
        return FileUtils.isFileExist(file);
    }

    /**
     * 获取是否存在
     *
     * @param file
     * @return true 已经存在，false 未存在
     */
    public static boolean isExist(File file) {
        return FileUtils.isFileExist(file);
    }

    /**
     * 创建文件
     *
     * @param filename 文件名称
     * @return 成功则返回 file，否则返回 null
     */
    public static File createFile(String filename) {
        File file = getFile(filename);
        return FileUtils.createFile(file) ? file : null;
    }

    /**
     * 创建文件夹
     *
     * @param folderName 文件夹名称
     * @return 成功则返回 file，否则返回 null
     */
    public static File createFolder(String folderName) {
        File file = getFile(folderName);
        return FileUtils.createFolder(file) ? file : null;
    }

    /**
     * 创建文件
     *
     * @param file 文件名称
     * @return 成功则返回 file，否则返回 null
     */
    public static File createFile(File file) {
        return FileUtils.createFile(file) ? file : null;
    }

    /**
     * 删除文件
     *
     * @param filename 文件名字
     * @return true 删除成功，false 删除失败
     */
    public static boolean deleteFile(String filename) {
        File file = getFile(filename);
        return FileUtils.deleteFile(file);
    }

    /**
     * 删除文件夹
     *
     * @param folderName 文件夹名字
     * @return true 删除成功，false 删除失败
     */
    public static boolean deleteFolder(String folderName) {
        File file = getFile(folderName);
        return FileUtils.deleteFolder(file);
    }

    /**
     * 删除文件
     *
     * @param file
     * @return true 删除成功，false 删除失败
     */
    public static boolean deleteFile(File file) {
        return FileUtils.deleteFile(file);
    }

    /**
     * 获取文件
     *
     * @param filename 文件名
     * @return 文件
     */
    public static File getFile(String filename) {
        return new File(getFolder(), filename);
    }

}
