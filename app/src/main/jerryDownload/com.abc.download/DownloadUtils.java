package com.abc.download;

import com.bit.cache.AppCache;

/**
 * author       : frog
 * time         : 2019-10-28 11:07
 * desc         :
 * version      :
 */
public class DownloadUtils {

    private static final String DOWNLOAD_NAME = "DOWNLOAD_NAME";

    private static final Long NO_DATA = -9999L;
    private static final long DURATION = 7 * 24 * 60 * 60 * 1000L;

    // file/download/[文件id]/[文件名]
    private static final String DOWNLOAD_PATH = "file/download/";

    private static long getDownload() {
        return AppCache.getInstance().getLong(DOWNLOAD_NAME);
    }

    /**
     * 是否需要显示下载提示框
     *
     * @return true 需要显示；false 不需要显示
     */
    public static boolean isNeedShowDownloadTip() {
        long lastTime = getDownload();

        if (lastTime == NO_DATA) {
            return true;
        }

        long offset = System.currentTimeMillis() - lastTime;

        if (offset > DURATION) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 更新提示时间
     */
    public static void uploadDate() {
        AppCache.getInstance().setCache(DOWNLOAD_NAME, System.currentTimeMillis());
    }

    /**
     * 重置
     */
    public static void reset() {
        AppCache.getInstance().setCache(DOWNLOAD_NAME, NO_DATA);
    }

}
