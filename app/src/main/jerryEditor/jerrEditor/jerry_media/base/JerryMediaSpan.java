package jerrEditor.jerry_media.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import com.zinc.lib_jerry_editor.config.LabelType;
import com.zinc.lib_jerry_editor.span.base.IBlockStyle;
import com.zinc.lib_jerry_editor.span.base.IJerryMediaSpan;
import com.zinc.lib_jerry_editor.span.base.IJerrySpan;

/**
 * author       : frog
 * time         : 2019-07-09 14:36
 * desc         : 多媒体 span基类
 * version      : 1.3.0
 */
public abstract class JerryMediaSpan<MEDIA_TYPE extends MediaInfo, INFO>
        extends ImageSpan
        implements IJerrySpan, IJerryMediaSpan<INFO>, IBlockStyle {

    protected MEDIA_TYPE mediaInfo;

    public JerryMediaSpan(Context context,
                          Bitmap bitmapDrawable,
                          MEDIA_TYPE media_type) {
        super(context, bitmapDrawable);
        this.mediaInfo = media_type;
    }

    public JerryMediaSpan(Drawable drawable,
                          MEDIA_TYPE media_type) {
        super(drawable);
        this.mediaInfo = media_type;
    }

    @Override
    public int getLabelType() {
        return LabelType.SINGLE;
    }

    @Override
    public String getOpenLabel() {
        return null;
    }

    @Override
    public String getCloseLabel() {
        return null;
    }

    @Override
    public String getAttr() {
        return null;
    }

}
