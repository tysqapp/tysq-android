package jerrEditor.jerry_media.style;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zinc.lib_jerry_editor.config.JerryConfig;
import com.zinc.lib_jerry_editor.utils.ImageUtils;

import jerrEditor.jerry_media.base.JerryMediaStyle;
import jerrEditor.jerry_media.model.ImgInfo;
import jerrEditor.jerry_media.span.JerryImgSpan;

/**
 * author       : frog
 * time         : 2019-07-08 09:06
 * desc         : 图片样式
 * version      : 1.3.0
 */

public class JerryImgStyle extends JerryMediaStyle<JerryImgSpan, ImgInfo> {

    @Override
    public void addOrCancelStyle(Editable editable,
                                 int start,
                                 int end,
                                 Object... params) {

        ImgInfo imgInfo = null;

        if (params.length > 0) {
            if (params[0] instanceof ImgInfo) {
                imgInfo = (ImgInfo) params[0];
            }
        }

        if (imgInfo == null) {
            return;
        }

        final Editable tempEditable = editable;
        final ImgInfo tempImgInfo = imgInfo;

        RequestOptions op = new RequestOptions()
                .error(imgInfo.getPlaceholder())
                .placeholder(imgInfo.getPlaceholder());

        JerryConfig.getGlideRequest()
                .asBitmap()
                .apply(op)
                .load(imgInfo.getUrl() == null ?
                        imgInfo.getPlaceholder() :
                        imgInfo.getUrl())
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap,
                                                @Nullable Transition<? super Bitmap> transition) {
                        bitmap = ImageUtils.scaleBitmapToFitWidth(bitmap,
                                JerryConfig.getJerryWidth());
                        addMedia(tempEditable,
                                bitmap,
                                tempImgInfo);
                    }
                });

    }


    @Override
    protected JerryImgSpan obtainSpan(Context context, Bitmap bitmap, ImgInfo imgInfo) {
        return new JerryImgSpan(context, bitmap, imgInfo);
    }
}
