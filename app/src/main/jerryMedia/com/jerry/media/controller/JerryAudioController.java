package com.jerry.media.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.jerry.media.utils.JerryAudioHelper;
import com.jerry.media.listener.JerryStatusListener;
import com.jerry.media.model.MediaInfo;

/**
 * author       : frog
 * time         : 2019-06-27 10:13
 * desc         : 音频控制器
 * version      : 1.3.0
 */
public class JerryAudioController implements Player.EventListener, IJerryAudioController {

    public static final String TAG = "JerryAudioController";

    // 是否开始测试日志
    public static final boolean IS_DEBUG = true;

    // Exo 播放器
    private ExoPlayer mExoPlayer;

    // 切换主线程的 handler
    private Handler mHandler;

    private Runnable mRunnable;

    // 播放信息
    private MediaInfo mMediaInfo;

    private MediaSource mMediaSource;

    // 是否结束
    private boolean isFinish;

    public JerryAudioController(Context context) {
        this.mExoPlayer = ExoPlayerFactory.newSimpleInstance(context);
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mRunnable = new UpdateRunnable();

        this.mMediaInfo = null;

        this.isFinish = false;

        initPlayer(mExoPlayer);
    }

    private void initPlayer(ExoPlayer exoPlayer) {
        // 准备好，不立马播放，因为要调用去除 loading 状态
        exoPlayer.setPlayWhenReady(false);
        // 设置监听器
        exoPlayer.addListener(this);
    }

    /**
     * 播放状态改变回调
     *
     * @param playWhenReady 是否准备完之后立马播放
     * @param playbackState 播放状态
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        String stateString;

        switch (playbackState) {

            case Player.STATE_IDLE:         // 暂停
                stateString = "ExoPlayer.STATE_IDLE      -";
                pause();
                break;

            case Player.STATE_BUFFERING:    // 缓冲
                stateString = "ExoPlayer.STATE_BUFFERING -";
                // loading 回调
                loading();
                break;

            case Player.STATE_READY:        // 就绪
                stateString = "ExoPlayer.STATE_READY     -";
                ready();
                break;

            case Player.STATE_ENDED:        // 结束
                stateString = "ExoPlayer.STATE_ENDED     -";
                end();
                break;

            default:
                stateString = "UNKNOWN_STATE             -";
                error("UNKNOWN_STATE");
                break;
        }

        if (IS_DEBUG) {
            Log.i(TAG, "--\n" +
                    (mMediaInfo == null ? "null" : mMediaInfo.toString()) + "\n " +
                    "[changed state to " + stateString + "]\n" +
                    "[playWhenReady: " + playWhenReady + "]");
        }

    }

    /**
     * 结束播放
     */
    private void end() {
        releaseRunnable();

        if (mMediaInfo == null) {
            return;
        }

        mMediaInfo.setStatus(MediaInfo.ENDED);

        if (mMediaInfo.getListener() != null) {
            mMediaInfo.getListener().playStatusChange();
        }

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        error(error.getMessage());
    }

    /**
     * 播放音频
     *
     * @param mediaInfo 播放音频信息
     */
    @Override
    public void play(MediaInfo mediaInfo) {

        if (mMediaInfo != null
                && mMediaInfo.getStatus() == MediaInfo.ENDED) {
            mMediaInfo.setCurPos(0);
        }

        if (mMediaInfo != null) {
            mMediaInfo.setBuffered(0);
        }

        // 同一个音频不需要释放
        if (!JerryAudioHelper.isSameAudio(mMediaInfo, mediaInfo)) {
            releaseMediaSource(mMediaInfo);
            mMediaInfo = mediaInfo;
            mMediaSource = JerryAudioHelper.buildMediaSource(mMediaInfo.getUri());
            mExoPlayer.prepare(mMediaSource, true, false);

        } else {
            // 准备播放资源
            mExoPlayer.prepare(mMediaSource, false, false);
        }

        if (mMediaInfo != null
                && mMediaInfo.getCurPos() != MediaInfo.NONE) {
            mExoPlayer.seekTo(mMediaInfo.getCurPos());
        }

        if (mMediaInfo == null) {
            return;
        }

        // 将当前资源置为准备状态
        mMediaInfo.setStatus(MediaInfo.INIT);

        if (mMediaInfo.getListener() != null) {
            mMediaInfo.getListener().playStatusChange();
        }
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        mExoPlayer.stop();

        releaseRunnable();

        if (mMediaInfo == null) {
            return;
        }

        mMediaInfo.setStatus(MediaInfo.PAUSE);

        if (mMediaInfo.getListener() != null) {
            mMediaInfo.getListener().playStatusChange();
        }
    }

    /**
     * 进度回调
     */
    private void progress() {
        if (mMediaInfo == null) {
            return;
        }

        // 设置当前的下标
        if (!mMediaInfo.isMoveBar()) {
            mMediaInfo.setCurPos(mExoPlayer.getCurrentPosition());
        }

        mMediaInfo.setBuffered(mExoPlayer.getBufferedPosition());

        if (mMediaInfo.getListener() != null) {
            mMediaInfo.getListener().progressChange();
        }
    }

    /**
     * loading 中的状态
     */
    private void loading() {
        if (mMediaInfo == null) {
            return;
        }

        mMediaInfo.setStatus(MediaInfo.INIT);

        if (mMediaInfo.getListener() != null) {
            mMediaInfo.getListener().playStatusChange();
        }

    }

    /**
     * 就绪回调
     */
    private void ready() {

        // 进度回调
        progress();

        // 轮训拿取进度
        isFinish = false;
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);

        if (mMediaInfo == null) {
            return;
        }

        // 开始播放
        mExoPlayer.setPlayWhenReady(true);
        // 获取时长
        mMediaInfo.setDuration(mExoPlayer.getDuration());
        // 改变状态
        mMediaInfo.setStatus(MediaInfo.PLAY);

        if (mMediaInfo.getListener() != null) {
            mMediaInfo.getListener().playStatusChange();
        }

    }

    /**
     * 错误回调
     *
     * @param msg
     */
    private void error(String msg) {
        mExoPlayer.stop();

        if (mMediaInfo == null) {
            return;
        }

        mMediaInfo.setStatus(MediaInfo.ERROR);

        if (mMediaInfo.getListener() != null) {
            mMediaInfo.getListener().tipChange(JerryStatusListener.ERROR, msg);
        }

    }

    /**
     * 释放多媒体资源
     */
    private void releaseMediaSource(MediaInfo mediaInfo) {
        if (mediaInfo == null) {
            return;
        }

        // 将音频设置为暂停
        mediaInfo.setStatus(MediaInfo.PAUSE);

        if (mediaInfo.getListener() == null) {
            return;
        }

        // 通知状态变动
        mediaInfo.getListener().playStatusChange();
    }

    /**
     * 移动
     */
    public void seekTo(MediaInfo mediaInfo) {
        if (!JerryAudioHelper.isSameAudio(mediaInfo, mMediaInfo)) {
            return;
        }

        mExoPlayer.seekTo(mediaInfo.getCurPos());
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {

        this.mMediaInfo = null;

        releaseRunnable();

        this.mHandler = null;
        this.mRunnable = null;

        this.mExoPlayer.stop();
        this.mExoPlayer.release();
        this.mExoPlayer = null;

        this.mMediaSource = null;
    }

    /**
     * 释放 runnable
     */
    private void releaseRunnable() {
        this.mHandler.removeCallbacks(mRunnable);
        this.isFinish = true;
    }

    private class UpdateRunnable implements Runnable {

        private long beforeTime;

        @Override
        public void run() {

            progress();

            if (IS_DEBUG) {
                beforeTime = System.currentTimeMillis() - beforeTime;

                Log.i(TAG, "run...." + beforeTime);

                beforeTime = System.currentTimeMillis();
            }

            if (isFinish) {
                return;
            }

            mHandler.postDelayed(this, 300);
        }
    }

}
