package request;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019/6/6 上午11:19
 * desc         : 封面更换
 * version      : 1.3.0
 */
public class VideoCoverReq {

    @SerializedName("cover_id")
    private int coverId;
    @SerializedName("video_id")
    private int videoId;

    public VideoCoverReq(int coverId, int videoId) {
        this.coverId = coverId;
        this.videoId = videoId;
    }

    public int getCoverId() {
        return coverId;
    }

    public void setCoverId(int coverId) {
        this.coverId = coverId;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }
}
