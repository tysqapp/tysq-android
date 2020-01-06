package com.abc.lib_utils;

import android.media.MediaMetadataRetriever;

/**
 * author       : frog
 * time         : 2019-09-24 11:06
 * email        : xxxxx
 * desc         : 视频工具
 * version      : 1.0.0
 */

public class VideoUtils {

    /**
     * 获取本地视频长度
     *
     * @param videoPath 视频路径
     * @return 视频时长，如果获取异常则返回-1
     */
    public static int getLocalVideoDuration(String videoPath) {
        int duration;

        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(videoPath);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            duration = Integer.parseInt(durationStr);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return duration;
    }

}
