package jerrEditor.jerry_media.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;

import com.zinc.lib_jerry_editor.config.JerryConfig;
import com.zinc.lib_jerry_editor.config.JerryConstant;
import com.zinc.lib_jerry_editor.model.EditInfo;
import com.zinc.lib_jerry_editor.style.common.JerryAbsCommonStyle;

/**
 * author       : frog
 * time         : 2019-07-09 11:30
 * desc         : 多媒体样式基类
 * version      : 1.3.0
 */
public abstract class JerryMediaStyle<SPAN, MEDIA_MODEL extends MediaInfo>
        extends JerryAbsCommonStyle<SPAN> {

    @Override
    public SPAN obtainSpan() {
        return null;
    }

    @Override
    public boolean isCheck() {
        return false;
    }

    @Override
    public void insertContent(EditInfo editInfo) {
        // 空实现
    }

    @Override
    protected int obtainFlag() {
        return Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;
    }

    @Override
    public boolean checkConsumeDeleteEvent(Editable editable, int start, int end) {
        return false;
    }

    protected void addMedia(Editable editable,
                            Bitmap bitmap,
                            MEDIA_MODEL model) {

        int start = getJerryEditor().getSelectionStart();
        int end = getJerryEditor().getSelectionEnd();

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

        int startTemp = 0;

        int prePos = start - 1;
        if (prePos >= 0) {
            char preChar = editable.charAt(prePos);
            if (preChar != JerryConstant.ENTER) {
                ++startTemp;
                stringBuilder.append(JerryConstant.ENTER);
            }
        } else {
            ++startTemp;
            stringBuilder.append(JerryConstant.ENTER);
        }

        stringBuilder.append(JerryConstant.ZERO_WIDTH_SPACE_STR);

        int nextPos = end + 1;
        if (nextPos < editable.length()) {
            char nextChar = editable.charAt(nextPos);
            if (nextChar != JerryConstant.ENTER) {
                stringBuilder.append(JerryConstant.ENTER);
            }
        } else {
            stringBuilder.append(JerryConstant.ENTER);
        }

        stringBuilder.setSpan(
                obtainSpan(JerryConfig.getContext(),
                        bitmap,
                        model),
                startTemp,
                startTemp + 1,
                obtainFlag());

        editable.replace(start, end, stringBuilder);

    }

    protected abstract SPAN obtainSpan(Context context,
                                       Bitmap bitmap,
                                       MEDIA_MODEL model);

    @Override
    public void setImageView(ImageView imageView) {
        this.mImageView = imageView;
    }

}
