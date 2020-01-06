package jerrEditor.jerry_media.model;

import jerrEditor.jerry_media.base.MediaInfo;
import model.VideoModel;

/**
 * author       : frog
 * time         : 2019-07-09 14:29
 * desc         : 视频信息
 * version      : 1.3.0
 */
public class VideoInfo extends MediaInfo {

    private VideoModel videoModel;

    private String coverUrl;

    private int placeholder;

    public VideoInfo(int id,
                     String url,
                     String coverUrl,
                     int placeholder,
                     VideoModel videoModel) {
        super(id, url);
        this.coverUrl = coverUrl;
        this.placeholder = placeholder;
        this.videoModel = videoModel;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public VideoModel getVideoModel() {
        return videoModel;
    }

    public void setVideoModel(VideoModel videoModel) {
        this.videoModel = videoModel;
    }
}
