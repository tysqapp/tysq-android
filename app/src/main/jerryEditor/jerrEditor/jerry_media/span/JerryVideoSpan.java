package jerrEditor.jerry_media.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import jerrEditor.config.JerryEditorConstant;
import jerrEditor.config.MediaType;
import jerrEditor.jerry_media.base.JerryMediaSpan;
import jerrEditor.jerry_media.model.VideoInfo;
import model.VideoModel;

/**
 * author       : frog
 * time         : 2019-07-08 17:50
 * desc         : 视频
 * version      : 1.3.0
 */

public class JerryVideoSpan extends JerryMediaSpan<VideoInfo, VideoModel> {

    public JerryVideoSpan(Context context,
                          Bitmap bitmapDrawable,
                          VideoInfo videoInfo) {
        super(context, bitmapDrawable, videoInfo);
    }

    public JerryVideoSpan(Drawable drawable,
                          VideoInfo videoInfo) {
        super(drawable, videoInfo);
    }

    @Override
    public String getSingleLabel() {
        return JerryEditorConstant.VIDEO;
    }

    @Override
    public VideoModel getInfo() {
        return mediaInfo.getVideoModel();
    }

    @Override
    public String getType() {
        return MediaType.VIDEO;
    }
}
