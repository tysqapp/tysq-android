package jerrEditor.jerry_media.model;

import jerrEditor.jerry_media.base.MediaInfo;

/**
 * author       : frog
 * time         : 2019-07-09 09:58
 * desc         : 多媒体信息
 * version      : 1.3.0
 */
public class ImgInfo extends MediaInfo {

    private int placeholder;

    public ImgInfo(int id,
                   String url,
                   int placeholder) {
        super(id, url);
        this.placeholder = placeholder;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }
}
