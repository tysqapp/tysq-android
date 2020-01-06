package com.bit.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.graphics.Palette;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bit.R;
import com.bit.utils.UIUtils;

/**
 * author       : frog
 * time         : 2019/4/16 下午4:17
 * desc         : 带进度条的webView
 * version      : 1.3.0
 */
public class ProgressWebView extends WebView {

    private ProgressBar mProgressbar;
    private OnStateListener mListener;

    public ProgressWebView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        mProgressbar = new ProgressBar(
                getContext(),
                null,
                android.R.attr.progressBarStyleHorizontal
        );
        mProgressbar.setProgressDrawable(ResourcesCompat
                .getDrawable(getResources(),
                        R.drawable.j_process_horizontal,
                        null)
        );
        mProgressbar.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT,
                        UIUtils.dip2px(getContext(), 3),
                        0,
                        0)
        );

        addView(mProgressbar);
        setWebChromeClient(new WebChromeClient());
    }

    public void addOnStateListener(OnStateListener listener) {
        this.mListener = listener;
    }

    private void onChange(WebView view, int newProgress) {
        if (mListener != null) {
            mListener.onChange(view, newProgress);
        }
    }

    private class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            onChange(view, newProgress);

            if (newProgress == 100) {
                mProgressbar.setVisibility(GONE);
            } else {
                if (mProgressbar.getVisibility() == GONE)
                    mProgressbar.setVisibility(VISIBLE);
                mProgressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mListener == null) {
                return;
            }
            mListener.onReceivedTitle(title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);

            if (mListener == null) {
                return;
            }

            Palette.from(icon)
                    .generate(palette -> {
                        Palette.Swatch vibrant = palette.getVibrantSwatch();

                        if (vibrant == null) {
                            for (Palette.Swatch swatch : palette.getSwatches()) {
                                if (swatch == null) {
                                    continue;
                                }
                                vibrant = swatch;
                                break;
                            }
                        }


                        if (vibrant == null) {
                            return;
                        }
                        int rbg = vibrant.getRgb();

                        mListener.onReceivedIcon(icon, changeColor(rbg));

                    });

        }

        private int changeColor(int rgb) {
            int red = rgb >> 16 & 0xFF;
            int green = rgb >> 8 & 0xFF;
            int blue = rgb & 0xFF;
            red = (int) Math.floor(red * (1 - 0.2));
            green = (int) Math.floor(green * (1 - 0.2));
            blue = (int) Math.floor(blue * (1 - 0.2));
            return Color.rgb(red, green, blue);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) mProgressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public interface OnStateListener {
        void onChange(WebView view, int newProgress);

        void onReceivedTitle(String title);

        void onReceivedIcon(Bitmap icon, int color);
    }

}