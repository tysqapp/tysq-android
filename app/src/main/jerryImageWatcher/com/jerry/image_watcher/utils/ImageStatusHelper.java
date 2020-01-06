package com.jerry.image_watcher.utils;

/**
 * author       : frog
 * time         : 2019-09-20 15:13
 * desc         : 图片状态协助
 * version      : 1.4.0
 */
public class ImageStatusHelper {

    // 显示正在加载
    public static final int IS_LOADING = 1;

    // 是否显示原图按钮
    public static final int IS_SHOW_ORIGINAL_BTN = 1 << 2;

    // 是否显示下载按钮
    public static final int IS_SHOW_DOWNLOAD = 1 << 3;
    // 是否第一次加载
//    public static final int IS_FIRST_LOADING = 1 << 4;

    /**
     * 添加状态
     */
    public static int addStatus(int status, int constant) {
        status |= constant;
        return status;
    }

    /**
     * 是否包含状态
     */
    public static boolean isContain(int status, int constant) {
        return (status & constant) == constant;
    }

    /**
     * 移除状态
     */
    public static int removeStatus(int status, int constant) {
        status &= ~constant;
        return status;
    }

}
