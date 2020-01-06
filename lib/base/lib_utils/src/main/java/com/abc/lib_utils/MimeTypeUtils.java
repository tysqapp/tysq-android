package com.abc.lib_utils;

/**
 * author       : frog
 * time         : 2019-09-24 11:05
 * email        : xxxxx
 * desc         : mimeType 工具类
 * version      : 1.0.0
 */

public final class MimeTypeUtils {

    public final static int TYPE_IMAGE = 1;
    public final static int TYPE_VIDEO = 2;
    public final static int TYPE_AUDIO = 3;
    public final static int TYPE_UNKNOW = 4;

    /**
     * 获取 mimeType 对应的类型
     *
     * @param mimeType 多媒体类型
     * @return 返回对应类型，如果匹配不到，则返回图像
     * {@link MimeTypeUtils#TYPE_IMAGE} 图像
     * {@link MimeTypeUtils#TYPE_VIDEO} 视频
     * {@link MimeTypeUtils#TYPE_AUDIO} 音频
     */
    public static int getMultimediaType(String mimeType) {
        switch (mimeType) {
            case "image/png":
            case "image/PNG":
            case "image/jpeg":
            case "image/JPEG":
            case "image/webp":
            case "image/WEBP":
            case "image/gif":
            case "image/bmp":
            case "image/GIF":
            case "imagex-ms-bmp":
                return TYPE_IMAGE;
            case "video/3gp":
            case "video/3gpp":
            case "video/3gpp2":
            case "video/avi":
            case "video/mp4":
            case "video/quicktime":
            case "video/x-msvideo":
            case "video/x-matroska":
            case "video/mpeg":
            case "video/webm":
            case "video/mp2ts":
                return TYPE_VIDEO;
            case "audio/mpeg":
            case "audio/x-ms-wma":
            case "audio/x-wav":
            case "audio/amr":
            case "audio/wav":
            case "audio/aac":
            case "audio/mp4":
            case "audio/quicktime":
            case "audio/lamr":
            case "audio/3gpp":
                return TYPE_AUDIO;
        }
        return TYPE_UNKNOW;
    }

    /**
     * 是否是gif
     *
     * @param mimeType 多媒体类型
     * @return 是 gif 则返回true，否则返回false
     */
    public static boolean isGif(String mimeType) {
        switch (mimeType) {
            case "image/gif":
            case "image/GIF":
                return true;
        }
        return false;
    }

    /**
     * 是否是视频
     *
     * @param mimeType 多媒体类型
     * @return 是 视频 则返回true，否则返回false
     */
    public static boolean isVideo(String mimeType) {
        switch (mimeType) {
            case "video/3gp":
            case "video/3gpp":
            case "video/3gpp2":
            case "video/avi":
            case "video/mp4":
            case "video/quicktime":
            case "video/x-msvideo":
            case "video/x-matroska":
            case "video/mpeg":
            case "video/webm":
            case "video/mp2ts":
                return true;
        }
        return false;
    }



}
