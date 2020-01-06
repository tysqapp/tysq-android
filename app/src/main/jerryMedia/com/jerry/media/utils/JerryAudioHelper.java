package com.jerry.media.utils;

import android.net.Uri;

import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.jerry.media.model.MediaInfo;

/**
 * author       : frog
 * time         : 2019-06-27 10:29
 * desc         :
 * version      : 1.3.0
 */
public class JerryAudioHelper {

    public static MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    /**
     * 判断是否为同一个音频
     *
     * @return true：为同一个；false：为不同个
     */
    public static boolean isSameAudio(MediaInfo aMediaInfo, MediaInfo bMediaInfo) {
        if (aMediaInfo == null || bMediaInfo == null) {
            return false;
        }
        return aMediaInfo.getId().equals(bMediaInfo.getId());
    }
}
