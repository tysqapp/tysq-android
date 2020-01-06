package com.tysq.ty_android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.bit.utils.UIUtils;
import com.tysq.ty_android.R;

/**
 * author       : frog
 * time         : 2019-07-16 15:54
 * desc         : 圆角
 * version      : 1.3.0
 */
public class RoundRectView extends View {

    private Paint mPaint;

    private Path mPath;

    private RectF mRect;

    private int mRoundSize;

    public RoundRectView(Context context) {
        this(context, null);
    }

    public RoundRectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPaint.setColor(ContextCompat.getColor(context, R.color.orange_red_color));
        mPaint.setStyle(Paint.Style.FILL);

        mRoundSize = UIUtils.dip2px(context, 4);

        mRect = new RectF();

        mPath = new Path();
    }

    private int mWidth;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = Math.min(w, h);

        mRect.set(mWidth - mRoundSize * 2, 0, mWidth, mRoundSize * 2);

        mPath.moveTo(0, 0);
        mPath.lineTo(mWidth - mRoundSize, 0);
        mPath.addArc(mRect, -90, 90);
        mPath.lineTo(mWidth, mWidth);
        mPath.lineTo(0, 0);
        mPath.close();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

}
