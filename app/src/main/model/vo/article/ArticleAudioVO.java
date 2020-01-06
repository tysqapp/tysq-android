package vo.article;

import com.jerry.media.model.MediaInfo;

/**
 * author       : frog
 * time         : 2019/5/20 上午11:32
 * desc         :
 * version      : 1.3.0
 */
public class ArticleAudioVO {

    private int id;

    private String url;
    private String audioName;

    private MediaInfo mediaInfo;

    public ArticleAudioVO(int id,
                          String url,
                          String audioName,
                          MediaInfo mediaInfo) {
        this.id = id;
        this.url = url;
        this.audioName = audioName;
        this.mediaInfo = mediaInfo;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getAudioName() {
        return audioName;
    }

    public MediaInfo getMediaInfo() {
        return mediaInfo;
    }
}
