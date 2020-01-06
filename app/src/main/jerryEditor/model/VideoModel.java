package model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : frog
 * time         : 2019/5/17 下午4:58
 * desc         : 发布文章——视频列表
 * version      : 1.3.0
 */
public class VideoModel {

    /**
     * video : 1
     * cover : [3,2,5]
     * screen_shot : [1,2]
     */

    @SerializedName("video")
    private final int video;
    @SerializedName("cover")
    private final List<Integer> cover;
    @SerializedName("screenshot")
    private final List<Integer> screenShot;

    public VideoModel(int video) {
        this.video = video;
        this.cover = new ArrayList<>();
        this.screenShot = new ArrayList<>();
    }

    public int getVideo() {
        return video;
    }

    public List<Integer> getCover() {
        return cover;
    }

    public List<Integer> getScreenShot() {
        return screenShot;
    }

    @Override
    public String toString() {
        return "VideoModel{" +
                "video=" + video +
                ", cover=" + cover +
                ", screenShot=" + screenShot +
                '}';
    }
}
