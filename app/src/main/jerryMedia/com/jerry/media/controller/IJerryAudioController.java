package com.jerry.media.controller;

import com.jerry.media.model.MediaInfo;

/**
 * author       : frog
 * time         : 2019-06-28 10:08
 * desc         :
 * version      : 1.3.0
 */
public interface IJerryAudioController {

    /**
     * 播放
     *
     * @param mediaInfo 播放信息
     */
    void play(MediaInfo mediaInfo);

    /**
     * 暂停
     */
    void pause();

    /**
     * 跳进度
     *
     * @param mediaInfo 播放信息
     */
    void seekTo(MediaInfo mediaInfo);

    /**
     * 释放
     */
    void release();

}
