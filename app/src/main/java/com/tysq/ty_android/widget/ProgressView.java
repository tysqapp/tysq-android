package com.tysq.ty_android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.bit.utils.UIUtils;
import com.tysq.ty_android.R;

/**
 * author       : frog
 * time         : 2019/5/29 下午5:34
 * desc         : 进度条
 * version      : 1.3.0
 */
public class ProgressView extends View {

    private static final String PERCENT = "%";

    private int mCircleColor;
    private int mCurColor;
    private Paint mPaint;
    private int mTextSize;
    private int mPerSize;

    private int mCircleWidth;
    private int mCurWidth;

    private int mProgress = 0;

    private RectF mRectF;
    private RectF mDotRectF;

    private SweepGradient mSweepGradient;

    private float[] pos;
    private int[] color;

    private boolean isDrawText;

    public ProgressView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 设置进度
     *
     * @param progress 0-100
     */
    public void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        } else if (progress > 100) {
            progress = 100;
        }

        this.mProgress = progress;

        postInvalidate();
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mCircleColor = ContextCompat.getColor(context, R.color.progress_circle_color);
        mCurColor = ContextCompat.getColor(context, R.color.progress_start_color);

        mTextSize = UIUtils.dip2px(context, 11);
        mPerSize = UIUtils.dip2px(context, 9);

        mCircleWidth = UIUtils.dip2px(context, 1.5f);
        mCurWidth = UIUtils.dip2px(context, 3f);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mRectF = new RectF();
        mDotRectF = new RectF();
        mDotRectF.left = -1;
        mDotRectF.top = -1;
        mDotRectF.right = mCurWidth;
        mDotRectF.bottom = mCurWidth;

        pos = new float[]{0, 1};
        color = new int[]{
                ContextCompat.getColor(context, R.color.progress_start_color),
                ContextCompat.getColor(context, R.color.progress_end_color)
        };

        if (attrs != null) {

            TypedArray typedArray = context
                    .getTheme()
                    .obtainStyledAttributes(attrs,
                            R.styleable.ProgressView,
                            defStyleAttr,
                            0);

            isDrawText = typedArray.getBoolean(R.styleable.ProgressView_draw_text, true);

            // 回收
            typedArray.recycle();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();
        int height = getHeight();

        canvas.translate(width / 2, height / 2);

        int padding = Math.max(mCircleWidth, mCurWidth);
        int radius = getWidth() / 2 - padding;
        drawCircle(canvas, radius);

        float percent = mProgress / 100f;
        drawProcess(canvas, radius, percent * 360f);

        drawText(canvas);

    }

    /**
     * 画圆
     *
     * @param canvas 画布
     * @param radius 半径
     */
    private void drawCircle(Canvas canvas, float radius) {
        mPaint.setColor(mCircleColor);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(0, 0, radius, mPaint);
    }

    /**
     * 画进度
     *
     * @param canvas     画布
     * @param radius     半径
     * @param sweepAngle 进度
     */
    private void drawProcess(Canvas canvas, float radius, float sweepAngle) {

        canvas.save();

        canvas.rotate(-90);

        // 初始化范围
        if (mRectF.isEmpty()) {
            mRectF.left = -radius;
            mRectF.top = -radius;
            mRectF.right = radius;
            mRectF.bottom = radius;
        }

        if (mSweepGradient == null) {
            mSweepGradient = new SweepGradient(
                    0, 0,
                    color, pos
            );
        }

        mPaint.setShader(mSweepGradient);
        mPaint.setColor(mCurColor);
        mPaint.setStrokeWidth(mCurWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(mRectF, 0,
                sweepAngle, false, mPaint);

        mPaint.setShader(null);

        canvas.restore();

        if (sweepAngle != 0) {
            canvas.save();
            canvas.translate(-mCurWidth / 2, -radius - mCurWidth / 2);
            mPaint.setColor(mCurColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawArc(mDotRectF, -270, 180, true, mPaint);
            canvas.restore();
        }

    }

    /**
     * 画字
     */
    private void drawText(Canvas canvas) {

        if (!isDrawText) {
            return;
        }

        // 测量进度字体的大小
        mPaint.setTextSize(mTextSize);
        String processText = mProgress + "";
        float processWidth = mPaint.measureText(processText);

        // 测量百分号的大小
        mPaint.setTextSize(mPerSize);
        float percentWidth = mPaint.measureText(PERCENT);
        float descent = mPaint.getFontMetrics().descent;

        // 总宽度
        float allWidth = processWidth + percentWidth;

        // 画百分号
        mPaint.setTextSize(mPerSize);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mCurColor);
        canvas.drawText(PERCENT,
                allWidth / 2 - percentWidth / 2,
                mTextSize - mPerSize - descent + mTextSize / 3,
                mPaint);

        // 画进度
        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mCurColor);
        canvas.drawText(processText,
                -(allWidth / 2 - processWidth / 2),
                mTextSize / 3,
                mPaint);
    }

}
