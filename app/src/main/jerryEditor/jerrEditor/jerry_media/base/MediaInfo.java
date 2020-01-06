package jerrEditor.jerry_media.base;

/**
 * author       : frog
 * time         : 2019-07-09 14:23
 * desc         : 多媒体基础信息
 * version      : 1.3.0
 */
public class MediaInfo {

    // 多媒体的id
    private int id;

    // 多媒体的url
    private String url;

    public MediaInfo(int id, String url) {
        this.id = id;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
