package jerrEditor.getter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.tysq.ty_android.R;
import com.zinc.lib_banner.JerryBannerUtil;
import com.zinc.lib_jerry_editor.config.JerryConfig;
import com.zinc.lib_jerry_editor.utils.ImageUtils;

public class JerryUrlDrawable extends BitmapDrawable {
    protected Drawable defaultDrawable;

    private Drawable mDrawable;
    protected int w;
    protected int h;

    private Context mContext;

    @SuppressWarnings("deprecation")
    public JerryUrlDrawable(Context context) {
        this.mContext = context;

        defaultDrawable = context.getResources()
                .getDrawable(R.drawable.placeholder_loading);

        BitmapFactory.Options options = new BitmapFactory.Options();

        // 只获取其宽高
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(this.mContext.getResources(),
                R.drawable.placeholder_loading,
                options);

        this.w = options.outWidth;
        this.h = options.outHeight;

        int jerryWidth = JerryConfig.getJerryWidth();

        float realHeight = h / (w * 1.0f) * jerryWidth;

        defaultDrawable.setBounds(0, 0, jerryWidth, (int) realHeight);

        Rect rect = new Rect(0, 0, w, h);
        this.setBounds(rect);
    }

    @Override
    public void draw(Canvas canvas) {
        Drawable drawable = mDrawable == null ? defaultDrawable : mDrawable;
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }

}
