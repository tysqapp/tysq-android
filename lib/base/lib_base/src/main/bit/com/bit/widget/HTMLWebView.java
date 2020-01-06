package com.bit.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HTMLWebView extends WebView {

    private static final String TAG = HTMLWebView.class.getSimpleName();

    public static final String CODING = "UTF-8";
    public static final String MIME_TYPE = "text/html";

    private OnUrlClickListener mOnUrlClickListener;
    private OnLoadFinishListener mOnLoadFinishListener;

    public HTMLWebView(Context context) {
        this(context, null, 0);
    }

    public HTMLWebView(Context context,
                       AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HTMLWebView(Context context,
                       AttributeSet attrs,
                       int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setHtml(String html) {
        loadDataWithBaseURL("",
                html,
                MIME_TYPE,
                CODING,
                "");
    }

    public void setOnUrlClickListener(OnUrlClickListener onUrlClickListener) {
        this.mOnUrlClickListener = onUrlClickListener;
    }

    public void setOnLoadFinishListener(OnLoadFinishListener onLoadFinishListener) {
        this.mOnLoadFinishListener = onLoadFinishListener;
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void init(Context context) {

        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setDefaultTextEncodingName("UTF-8");

        this.getSettings().setDomStorageEnabled(true);

        //水平不显示
        this.setHorizontalScrollBarEnabled(false);
        //垂直不显示
        this.setVerticalScrollBarEnabled(false);
        //取消滚动条白边效果
        this.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setLoadWithOverviewMode(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getSettings().setMixedContentMode(WebSettings
                    .MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }

        this.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url == null) {
                    return true;
                }

                if (url.isEmpty()) {
                    return true;
                }

                if (mOnUrlClickListener == null) {
                    return true;
                }

                return mOnUrlClickListener.urlClicked(view, url);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "onPageFinished: " + url);
                if (mOnLoadFinishListener == null) {
                    return;
                }

                mOnLoadFinishListener.pageFinish(view, url);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                Log.i(TAG, "onPageCommitVisible: " + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }
        });

    }

    public interface OnLoadFinishListener {

        /**
         * WebView 加载完毕回调
         */
        void pageFinish(WebView view, String url);

    }

    public interface OnUrlClickListener {

        /**
         * 超链接点击得回调方法
         *
         * @param url 点击得url
         * @return true：已处理，false：未处理（会进行默认处理）
         */
        boolean urlClicked(WebView view, String url);
    }

}