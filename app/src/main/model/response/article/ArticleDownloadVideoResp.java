package response.article;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-10-29 12:00
 * desc         :
 * version      :
 */
public class ArticleDownloadVideoResp {

    /**
     * is_satisfy 积分是否满足
     * limit_score 积分数量
     * video_url 下载视频地址
     * filename 文件名
     */

    @SerializedName("is_satisfy")
    private boolean isSatisfy;

    @SerializedName("limit_score")
    private int limitScore;

    @SerializedName("video_url")
    private String videoUrl;

    @SerializedName("filename")
    private String filename;

    public boolean isSatisfy() {
        return isSatisfy;
    }

    public void setSatisfy(boolean satisfy) {
        isSatisfy = satisfy;
    }

    public int getLimitScore() {
        return limitScore;
    }

    public void setLimitScore(int limitScore) {
        this.limitScore = limitScore;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
