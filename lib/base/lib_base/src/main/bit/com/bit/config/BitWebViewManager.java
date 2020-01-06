package com.bit.config;

import android.net.http.SslError;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

import com.bit.web.BitWebViewFragment;
import com.bit.web.IWebViewJavaScriptInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author a2
 * @date 创建时间：2018/4/23
 * @description
 */

public class BitWebViewManager {

    private static final BitWebViewManager ourInstance = new BitWebViewManager();

    public static BitWebViewManager getInstance() {
        return ourInstance;
    }

    //WebView的配置
    private IWebViewConfig mWebViewConfig;

    /**
     * js的接口回调类(只需要class，需要继承{@link IWebViewJavaScriptInterface})
     */
    private Class<? extends IWebViewJavaScriptInterface> mJavaScriptInterfaceClazz;

    private BitWebViewManager() {
        this.mWebViewConfig = new DefaultWebViewConfig();
        this.mJavaScriptInterfaceClazz = JDefaultJavaScriptInterface.class;
    }

    public IWebViewConfig getWebViewConfig() {
        return this.mWebViewConfig;
    }

    public void setWebViewConfig(IWebViewConfig webViewConfig) {
        this.mWebViewConfig = webViewConfig;
    }

    public Class<? extends IWebViewJavaScriptInterface> getJavaScriptInterfaceClazz() {
        return this.mJavaScriptInterfaceClazz;
    }

    public void setJavaScriptInterfaceClazz(Class<? extends IWebViewJavaScriptInterface> javaScriptInterfaceClazz) {
        this.mJavaScriptInterfaceClazz = javaScriptInterfaceClazz;
    }

    public static class DefaultWebViewConfig implements IWebViewConfig {

        private static final String TAG = DefaultWebViewConfig.class.getSimpleName();

        @Override
        public ArrayList<String> getWhiteList() {
            return new ArrayList<>();
        }

        @Override
        public Map<String, String> getHeader() {
            return new HashMap<>();
        }

        @Override
        public void jsCallBackApp(BitWebViewFragment fragment, String type, String content) {
//            FragmentActivity activity = fragment.getActivity();
//            Context context = fragment.getContext();
//            WebView webView = fragment.getWebView();

            Log.i(TAG, "jsCallBackApp: type"+type+";content"+content);

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

        }


    }

    public static class JDefaultJavaScriptInterface extends IWebViewJavaScriptInterface {

        @JavascriptInterface
        public void jsCallApp(String type, String content) {
            BitWebViewManager.getInstance().getWebViewConfig().jsCallBackApp(super.mFragment.get(), type, content);
        }

    }

}
