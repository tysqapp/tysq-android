package jerrEditor.getter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tysq.ty_android.R;
import com.zinc.lib_jerry_editor.JerryEditor;
import com.zinc.lib_jerry_editor.config.JerryConfig;
import com.zinc.lib_jerry_editor.config.glide.GlideOptions;
import com.zinc.lib_jerry_editor.utils.ImageUtils;

import jerrEditor.config.MediaType;
import jerrEditor.parser.Html;

public class JerryImageGetter implements Html.ImageGetter {

    private Context mContext;

    private JerryEditor mJerryEditor;

    private String mType;
    private String mFileName;

    public JerryImageGetter(Context context,
                            JerryEditor jerryEditor) {
        mContext = context;
        mJerryEditor = jerryEditor;
        mType = MediaType.IMAGE;
        mFileName = null;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public void setFileName(String fileName) {
        this.mFileName = fileName;
    }

    @Override
    public Drawable getDrawable(String source) {
        if (mContext == null) {
            return null;
        }

        JerryUrlDrawable jerryUrlDrawable = new JerryUrlDrawable(mContext);

        BitmapTarget bitmapTarget =
                new BitmapTarget(jerryUrlDrawable, mJerryEditor, mType, mFileName);

        if (mType.equalsIgnoreCase(MediaType.AUDIO)) {

            JerryConfig.getGlideRequest()
                    .asBitmap()
                    .load(R.drawable.img_audio)
                    .into(bitmapTarget);

        } else {

            GlideOptions options = new GlideOptions()
                    .error(R.drawable.placeholder_error)
                    .placeholder(R.drawable.placeholder_loading);

            JerryConfig.getGlideRequest()
                    .asBitmap()
                    .apply(options)
                    .load(source)
                    .into(bitmapTarget);

        }

        return jerryUrlDrawable;
    }

    private static class BitmapTarget extends SimpleTarget<Bitmap> {
        private final JerryUrlDrawable mJerryUrlDrawable;
        private JerryEditor mJerryEditor;
        private String mType;
        private String mFileName;

        private BitmapTarget(JerryUrlDrawable urlDrawable,
                             JerryEditor jerryEditor,
                             String type,
                             String fileName) {
            this.mJerryUrlDrawable = urlDrawable;
            this.mJerryEditor = jerryEditor;
            this.mType = type;
            this.mFileName = fileName;
        }

        @Override
        public void onResourceReady(@NonNull Bitmap bitmap,
                                    Transition<? super Bitmap> transition) {

            bitmap = ImageUtils.scaleBitmapToFitWidth(bitmap, JerryConfig.getJerryWidth());

            Bitmap result;

            // 如果为音频类型，则需要进行合并
            if (mType.equals(MediaType.VIDEO)) {
                Bitmap playBitmap = BitmapFactory
                        .decodeResource(
                                JerryConfig.getContext().getResources(),
                                R.drawable.ic_play_png);
                result = ImageUtils.mergeBitmaps(bitmap, playBitmap);
            } else {
                result = bitmap;
            }

            if (mType.equals(MediaType.AUDIO)) {
                result = ImageUtils.drawAudioText(JerryConfig.getContext(), result, mFileName);
            }

            int bw = result.getWidth();
            int bh = result.getHeight();

            Rect rect = new Rect(0, 0, bw, bh);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(result);
            bitmapDrawable.setBounds(rect);
            mJerryUrlDrawable.setBounds(rect);
            mJerryUrlDrawable.setDrawable(bitmapDrawable);

            mJerryEditor.setLoading(true);
            mJerryEditor.calculateContent();
            mJerryEditor.setLoading(false);

        }
    }

    public void release() {
        mContext = null;
        mJerryEditor = null;
    }
}
