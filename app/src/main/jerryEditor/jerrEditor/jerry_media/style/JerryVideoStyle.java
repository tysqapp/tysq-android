package jerrEditor.jerry_media.style;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tysq.ty_android.R;
import com.zinc.lib_jerry_editor.config.JerryConfig;
import com.zinc.lib_jerry_editor.utils.ImageUtils;

import jerrEditor.jerry_media.base.JerryMediaStyle;
import jerrEditor.jerry_media.model.VideoInfo;
import jerrEditor.jerry_media.span.JerryVideoSpan;

/**
 * author       : frog
 * time         : 2019-07-09 14:47
 * desc         : 视频样式
 * version      : 1.3.0
 */
public class JerryVideoStyle extends JerryMediaStyle<JerryVideoSpan, VideoInfo> {

    @Override
    protected JerryVideoSpan obtainSpan(Context context, Bitmap bitmap, VideoInfo videoInfo) {
        return new JerryVideoSpan(context, bitmap, videoInfo);
    }

    @Override
    public void addOrCancelStyle(Editable editable,
                                 int start,
                                 int end,
                                 Object... params) {

        VideoInfo videoInfo = null;

        if (params.length > 0) {
            if (params[0] instanceof VideoInfo) {
                videoInfo = (VideoInfo) params[0];
            }
        }

        if (videoInfo == null) {
            return;
        }

        final Editable tempEditable = editable;
        final VideoInfo tempVideoInfo = videoInfo;

        RequestOptions op = new RequestOptions()
                .error(videoInfo.getPlaceholder())
                .placeholder(videoInfo.getPlaceholder());

        JerryConfig.getGlideRequest()
                .asBitmap()
                .load(videoInfo.getCoverUrl() == null ?
                        videoInfo.getPlaceholder() :
                        videoInfo.getCoverUrl())
                .apply(op)
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap,
                                                @Nullable Transition<? super Bitmap> transition) {
                        bitmap = ImageUtils.scaleBitmapToFitWidth(bitmap,
                                JerryConfig.getJerryWidth());

                        Bitmap play = BitmapFactory.decodeResource(
                                JerryConfig.getContext().getResources(),
                                R.drawable.ic_play_png);

                        Bitmap result = ImageUtils.mergeBitmaps(bitmap, play);

                        addMedia(tempEditable, result, tempVideoInfo);
                    }
                });
    }

}
