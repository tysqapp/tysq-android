package com.tysq.ty_android.feature.web;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.login.LoginActivity;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.utils.TyUtils;

import java.lang.ref.WeakReference;

import webview.OpenNewLink;

public class TyJavaScriptInterface {
    public static final String TAG = TyJavaScriptInterface.class.getSimpleName();

    private final Gson mGson = new Gson();

    private final WeakReference<Context> mContext;

    public TyJavaScriptInterface(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    /**
     * 与js交互时用到的方法，在js里直接调用
     */
    @JavascriptInterface
    public void fromHtml(final String type, final String json) {
        Log.i(TAG, "fromHtml: [type: " + type + "; json:" + json + "]");

        switch (type) {
            case Constant.HtmlType.OPEN_NEW_LINK:
                OpenNewLink openNewLink = mGson.fromJson(json, OpenNewLink.class);

                if (openNewLink.isNeedLogin()) {
                    if (UserCache.isEmpty()) {
                        LoginActivity.startActivity(mContext.get(), null);
                        return;
                    }
                }

                String navigationLink = openNewLink.getNavigationLink();

                TyUtils.handleWebViewLink(mContext.get(), navigationLink);

                break;
        }
    }

    @JavascriptInterface
    public boolean openInAndroid() {
        return true;
    }
}
