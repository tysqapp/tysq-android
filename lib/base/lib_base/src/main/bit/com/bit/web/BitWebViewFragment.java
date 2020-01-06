package com.bit.web;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bit.config.BitWebViewManager;
import com.bit.view.fragment.BitBaseFragment;

/**
 * author       : frog
 * time         : 2019/3/26 下午3:29
 * desc         : WebView
 * version      : 1.3.0
 */
public class BitWebViewFragment extends BitBaseFragment
        implements ProgressWebView.OnStateListener {

    private static String TAG = BitWebViewFragment.class.getSimpleName();

    protected ProgressWebView mWebView;

    protected String mUrl;

    private WebListener mWebListener;

    public static BitWebViewFragment newInstance(String url) {
        BitWebViewFragment fragment = new BitWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("LOAD_URL", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setWebListener(WebListener webListener) {
        this.mWebListener = webListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);

        this.mUrl = arguments.getString("LOAD_URL");
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (container == null || container.getContext() == null) {
            return null;
        }

        mWebView = new ProgressWebView(container.getContext());
        mWebView.addOnStateListener(this);

        return mWebView;

    }

    @Override
    protected void initView(View view) {
        initWebView(mWebView);
        initWebViewSetting(mWebView.getSettings());

        mWebView.loadUrl(this.mUrl, BitWebViewManager.getInstance().getWebViewConfig().getHeader());
        WebView.setWebContentsDebuggingEnabled(true);
    }

    /**
     * 初始化 WebView
     */
    private void initWebView(WebView webView) {

        //第三方的cookie
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        webView.setWebViewClient(new BitWebViewClient());

        webView.setLongClickable(true);

    }

    private void initWebViewSetting(WebSettings settings) {

        //支持js脚本
        settings.setJavaScriptEnabled(true);
        //支持缩放
        settings.setSupportZoom(true);
        //支持缩放
        settings.setBuiltInZoomControls(true);
        //去除缩放按钮
        settings.setDisplayZoomControls(false);

        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);

        //多窗口
        settings.supportMultipleWindows();
        //关闭webview中缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true);
        //支持通过JS打开新窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //支持自动加载图片
        settings.setLoadsImagesAutomatically(true);

        //启用地理定位
//        settings.setGeolocationEnabled(true);
        //设置渲染优先级
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        // 设置支持本地存储
        settings.setDatabaseEnabled(true);
        //设置支持DomStorage
        settings.setDomStorageEnabled(true);

        addJavascriptInterface();

    }

    @SuppressLint("AddJavascriptInterface")
    protected void addJavascriptInterface() {
    }

    //处理WebView中回退的事件
    @Override
    public boolean onConsumeBackEvent(FragmentManager fragmentManager) {
        WebBackForwardList webBackForwardList = this.mWebView.copyBackForwardList();

        //如果有回退历史，而且可回退，就消费该事件
        if (webBackForwardList.getSize() > 0 && this.mWebView.canGoBack()) {
            this.mWebView.goBack();
            return true;
        }

        return super.onConsumeBackEvent(fragmentManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }

        super.onDestroy();
    }

    @Override
    public void onChange(WebView view, int newProgress) {

    }

    @Override
    public void onReceivedTitle(String title) {
        if (mWebListener == null) {
            return;
        }

        mWebListener.onReceivedTitle(title);
    }

    @Override
    public void onReceivedIcon(Bitmap icon, int color) {
        if (mWebListener == null) {
            return;
        }
        mWebListener.onReceivedIcon(icon, color);
    }

    private class BitWebViewClient extends WebViewClient {

        //https的ssl验证，需要弹框询问
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            /**
             * google建议，如果ssl需不需要通过，以弹框形式询问用户
             * 如果用户同意，调用{@link SslErrorHandler#proceed()}
             * 如果用户不同意，调用{@link SslErrorHandler#cancel()}
             */
            BitWebViewManager
                    .getInstance()
                    .getWebViewConfig()
                    .onReceivedSslError(view, handler, error);
        }

    }

    /**
     * 回调监听器
     */
    public interface WebListener {
        /**
         * 标题获取完后回调
         *
         * @param title 标题
         */
        void onReceivedTitle(String title);

        /**
         * 图标获取完回调
         *
         * @param icon  图标
         * @param color 图标主题颜色
         */
        void onReceivedIcon(Bitmap icon, int color);
    }

}
