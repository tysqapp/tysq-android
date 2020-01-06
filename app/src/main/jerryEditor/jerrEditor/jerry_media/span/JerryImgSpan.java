package jerrEditor.jerry_media.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import jerrEditor.config.JerryEditorConstant;
import jerrEditor.config.MediaType;
import jerrEditor.jerry_media.base.JerryMediaSpan;
import jerrEditor.jerry_media.model.ImgInfo;

/**
 * author       : frog
 * time         : 2019-07-08 17:50
 * desc         : 图片
 * version      : 1.3.0
 */

public class JerryImgSpan extends JerryMediaSpan<ImgInfo, Integer> {

    public JerryImgSpan(Context context,
                        Bitmap bitmapDrawable,
                        ImgInfo imgInfo) {
        super(context, bitmapDrawable, imgInfo);
    }

    public JerryImgSpan(Drawable drawable,
                        ImgInfo imgInfo) {
        super(drawable, imgInfo);
    }

    @Override
    public String getSingleLabel() {
        return JerryEditorConstant.IMG;
    }

    @Override
    public Integer getInfo() {
        return mediaInfo.getId();
    }

    @Override
    public String getType() {
        return MediaType.IMAGE;
    }
}
