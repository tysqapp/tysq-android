package com.tysq.ty_android.local.sp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.web.TyJavaScriptInterface;
import com.tysq.ty_android.utils.ScreenAdapterUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : liaozhenlin
 * time         : 2019/11/4 10:10
 * desc         : WebView的缓存
 * version      : 1.5.0
 */
public class WebViewCache {

    public static final int DEFAULT_WEBVIEW_NUM = 3;

    private final List<WebView> mWebViewList;

    public WebViewCache(Context context, String url, int size) {

        mWebViewList = new ArrayList<>(size);

        int width = ScreenAdapterUtils.getScreenWidth();
        int height = ScreenAdapterUtils.getAdHeight();

        for (int i = 0; i < DEFAULT_WEBVIEW_NUM; i++){
            WebView webView = new WebView(context);
            webView.loadUrl(url);

            ViewGroup.LayoutParams layoutParams = webView.getLayoutParams();

            if (layoutParams == null){
                layoutParams = new ViewGroup.LayoutParams(width, height);
            } else {
                layoutParams.width = width;
                layoutParams.height = height;
            }

            webView.setLayoutParams(layoutParams);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new TyJavaScriptInterface(context), "android");

            mWebViewList.add(webView);
        }
    }

    /**
     * 获取webview
     * @return
     */
    public WebView getWebView(){
        WebView webView = mWebViewList.remove(0);
        webView.stopLoading();
        return webView;
    }

    public List<WebView> getWebViewList(){ return  mWebViewList;}

    /**
     * 存储webview
     * @param webView
     */
    public void saveWebView(WebView webView){
        ViewGroup parent = (ViewGroup) webView.getParent();
        parent.removeView(webView);
        mWebViewList.add(webView);
    }

    /**
     * 销毁webview
     */
    public void destroyWebView(){
        if (mWebViewList == null || mWebViewList.size() <= 0){
            return;
        }

        for (int i = 0; i < mWebViewList.size(); i++){
            mWebViewList.get(i).destroy();
        }
    }

    /**
     * 重新加载webview
     */
    public void resumeWebView(){
        if (mWebViewList == null || mWebViewList.size() <= 0){
            return;
        }

        for (WebView webView : mWebViewList){
            webView.onResume();
        }
    }

    /**
     * 暂停webview
     */
    public void pauseWebView(){
        if (mWebViewList == null || mWebViewList.size() <= 0){
            return;
        }

        for (WebView webView : mWebViewList){
            webView.onPause();
        }
    }

}
