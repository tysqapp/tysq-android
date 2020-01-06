package jerrEditor.jerry_media.model;

import jerrEditor.jerry_media.base.MediaInfo;

/**
 * author       : frog
 * time         : 2019-07-09 11:24
 * desc         : 音频信息
 * version      : 1.3.0
 */
public class AudioInfo extends MediaInfo {

    private String title;

    public AudioInfo(int id, String url, String title) {
        super(id, url);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
