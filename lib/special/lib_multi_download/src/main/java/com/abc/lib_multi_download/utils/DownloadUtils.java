package com.abc.lib_multi_download.utils;

/**
 * author       : frog
 * time         : 2019-11-27 17:56
 * desc         : 下载
 * version      : 1.0.0
 */
public class DownloadUtils {

    private static final String DOT_CHAR = ".";

    /**
     * 文件名是否带有后缀
     *
     * @param fileName 文件名字
     * @return true：带有后缀；false：不该有后缀
     */
    public static boolean isExistSuffix(String fileName) {
        return fileName.contains(".") && !fileName.endsWith(".");
    }

    /**
     * 获取文件名，不带后缀
     *
     * @return xxx.suffix ===> xxx
     */
    public static String getFileNameWithoutSuffix(String fileName) {
        if (!isExistSuffix(fileName)) {
            return fileName;
        }

        int lastDotIndex = fileName.lastIndexOf(DOT_CHAR);

        return fileName.substring(0, lastDotIndex);
    }

    /**
     * 获取文件名，不带后缀
     *
     * @return xxx.suffix ===> suffix
     */
    public static String getFileNameSuffix(String fileName) {
        if (!isExistSuffix(fileName)) {
            return "";
        }

        int lastDotIndex = fileName.lastIndexOf(DOT_CHAR);

        return fileName.length() > (lastDotIndex + 1) ?
                fileName.substring(lastDotIndex + 1) : "";
    }

}
