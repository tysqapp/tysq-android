package com.tysq.ty_android.utils;

import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * author       : frog
 * time         : 2019-08-26 14:50
 * desc         : WebView cookie
 * version      : 1.3.0
 */
public class TyWebViewCookie {

    public static CookieManager getInstance() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        return cookieManager;
    }

    public static void sync() {
        CookieSyncManager.getInstance().sync();
    }

    public static void removeCookie() {
        CookieManager cookieManager = getInstance();
        cookieManager.removeSessionCookie();
        sync();
    }
}
