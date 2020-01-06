package jerrEditor.jerry_media.style;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tysq.ty_android.R;
import com.zinc.lib_jerry_editor.config.JerryConfig;
import com.zinc.lib_jerry_editor.utils.ImageUtils;

import jerrEditor.jerry_media.base.JerryMediaStyle;
import jerrEditor.jerry_media.model.AudioInfo;
import jerrEditor.jerry_media.span.JerryAudioSpan;

/**
 * author       : frog
 * time         : 2019-07-08 09:06
 * desc         : 图片样式
 * version      : 1.3.0
 */

public class  JerryAudioStyle extends JerryMediaStyle<JerryAudioSpan, AudioInfo> {

    @Override
    public void addOrCancelStyle(Editable editable,
                                 int start,
                                 int end,
                                 Object... params) {

        AudioInfo audioInfo = null;

        if (params.length > 0) {
            if (params[0] instanceof AudioInfo) {
                audioInfo = (AudioInfo) params[0];
            }
        }

        if (audioInfo == null) {
            return;
        }

        final Editable tempEditable = editable;
        final AudioInfo tempImgInfo = audioInfo;

        JerryConfig.getGlideRequest()
                .asBitmap()
                .load(R.drawable.img_audio)
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap,
                                                @Nullable Transition<? super Bitmap> transition) {
                        bitmap = ImageUtils.scaleBitmapToFitWidth(bitmap,
                                JerryConfig.getJerryWidth());

                        bitmap = ImageUtils.drawAudioText(JerryConfig.getContext(),
                                bitmap,
                                tempImgInfo.getTitle());

                        addMedia(tempEditable,
                                bitmap,
                                tempImgInfo);

                    }
                });

    }

    @Override
    protected JerryAudioSpan obtainSpan(Context context, Bitmap bitmap, AudioInfo audioInfo) {
        return new JerryAudioSpan(context, bitmap, audioInfo);
    }
}
