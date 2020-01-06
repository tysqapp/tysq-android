package com.tysq.ty_android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.bit.utils.UIUtils;

public class CustomRoundAngleImageView extends AppCompatImageView {

    private static final float CONNER_RADIUS = 10;

    private float mWidth;
    private float mHeight;
    private Path mPath;

    private float mConnerRadius;

    private RectF mRect;

    private Paint paint = new Paint();

    public CustomRoundAngleImageView(Context context) {
        this(context, null, 0);
    }

    public CustomRoundAngleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRoundAngleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mPath = new Path();
        mConnerRadius = UIUtils.dip2px(context, CONNER_RADIUS);
        mRect = new RectF();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPath.moveTo(mConnerRadius, 0);
        mPath.lineTo(mWidth - mConnerRadius, 0);

        mRect.set(mWidth - 2 * mConnerRadius,
                0,
                mWidth,
                2 * mConnerRadius);
        mPath.addArc(mRect, 270, 90);

        mPath.lineTo(mWidth, mHeight);

        mPath.lineTo(0, mHeight);

        mPath.lineTo(0, mConnerRadius);

        paint.setColor(Color.BLUE);

        mRect.set(0,
                0,
                mConnerRadius * 2,
                mConnerRadius * 2);
        mPath.addArc(mRect, 180, 90);

        mPath.lineTo(mConnerRadius, 0);
        mPath.lineTo(mWidth - mConnerRadius,  0);

        canvas.clipPath(mPath);

        super.onDraw(canvas);
    }

}
