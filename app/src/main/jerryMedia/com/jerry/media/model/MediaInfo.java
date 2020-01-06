package com.jerry.media.model;

import android.net.Uri;
import android.support.annotation.IntRange;

import com.jerry.media.listener.JerryStatusListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * author       : frog
 * time         : 2019-06-27 10:14
 * desc         : 多媒体信息
 * version      : 1.3.0
 */
public class MediaInfo {

    // 没有初始化
    public final static int NO_INIT = 1;
    // 初始化中
    public final static int INIT = 2;
    // 开始
    public final static int PLAY = 3;
    // 暂停
    public final static int PAUSE = 4;
    // 错误
    public final static int ERROR = 5;
    // 终止
    public final static int ENDED = 6;

    // 没有初始化的下标
    public final static int NONE = -1;

    // 音频id
    private String id;

    // 列表中的位置
    private int rvPos;
    // 资源url
    private final Uri uri;
    // 状态
    private int status;
    // 时长
    private long duration;
    // 当前播放时长
    private long curPos;
    // 缓冲
    private long buffered;

    // 是否正在移动进度
    private boolean isMoveBar;

    private JerryStatusListener listener;

    public MediaInfo(String url) {
        Uri uri = Uri.parse(url);

        String lastPathSegment = uri.getLastPathSegment();
        if(lastPathSegment != null){
            String encodeLastPathSegment = lastPathSegment;
            try {
                encodeLastPathSegment = URLEncoder.encode(lastPathSegment, "UTF8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if( encodeLastPathSegment != null){
                url = url.replace(lastPathSegment, encodeLastPathSegment);
            }
        }

        this.uri = Uri.parse(url);

        this.status = NO_INIT;
        this.duration = NONE;
        this.curPos = NONE;
        this.isMoveBar = false;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRvPos(int rvPos) {
        this.rvPos = rvPos;
    }

    public String getId() {
        return id;
    }

    public int getRvPos() {
        return rvPos;
    }

    public Uri getUri() {
        return uri;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(@IntRange(from = NO_INIT, to = ENDED) int status) {
        this.status = status;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getCurPos() {
        return curPos;
    }

    public void setCurPos(long curPos) {
        this.curPos = curPos;
    }

    public JerryStatusListener getListener() {
        return listener;
    }

    public void setListener(JerryStatusListener listener) {
        this.listener = listener;
    }

    public long getBuffered() {
        return buffered;
    }

    public void setBuffered(long buffered) {
        this.buffered = buffered;
    }

    public boolean isMoveBar() {
        return isMoveBar;
    }

    public void setMoveBar(boolean moveBar) {
        isMoveBar = moveBar;
    }

    @Override
    public String toString() {
        return "MediaInfo{" + "\n" +
                "rvPos=" + rvPos + "\n" +
                ", uri=" + uri.getPath() + "\n" +
                ", status=" + status + "\n" +
                ", duration=" + duration + "\n" +
                ", curPos=" + curPos + "\n" +
                "}";
    }
}
