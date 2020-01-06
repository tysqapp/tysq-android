package jerrEditor.jerry_media.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import jerrEditor.config.JerryEditorConstant;
import jerrEditor.config.MediaType;
import jerrEditor.jerry_media.base.JerryMediaSpan;
import jerrEditor.jerry_media.model.AudioInfo;

/**
 * author       : frog
 * time         : 2019-07-08 17:50
 * desc         : 音频
 * version      : 1.3.0
 */

public class JerryAudioSpan extends JerryMediaSpan<AudioInfo, Integer> {

    public JerryAudioSpan(Context context,
                          Bitmap bitmapDrawable,
                          AudioInfo audioInfo) {
        super(context, bitmapDrawable, audioInfo);
    }

    public JerryAudioSpan(Drawable drawable,
                          AudioInfo audioInfo) {
        super(drawable, audioInfo);
    }

    @Override
    public String getSingleLabel() {
        return JerryEditorConstant.AUDIO;
    }

    @Override
    public Integer getInfo() {
        return mediaInfo.getId();
    }

    @Override
    public String getType() {
        return MediaType.AUDIO;
    }
}
