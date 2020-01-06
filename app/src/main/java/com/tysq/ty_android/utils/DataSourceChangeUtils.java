package com.tysq.ty_android.utils;

import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.config.TyFragmentManager;
import com.tysq.ty_android.local.sp.NetCache;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.config.NetConfig;
import com.tysq.ty_android.net.cookie.PersistentCookieStore;

import cache.Net;

/**
 * author       : frog
 * time         : 2019-11-20 09:49
 * desc         : 数据源更改
 * version      : 1.5.2
 */
public class DataSourceChangeUtils {

    /**
     * 初始化html
     */
    public static void initHtml() {

        // 初始化 html
        Constant.HtmlUrl.initHtml(NetConfig.getBaseUrlWithoutVersion());
        // 初始化 api
        Constant.HtmlAPI.initHtml(NetConfig.getBaseUrlWithoutVersion());

    }

    /**
     * 清理缓存
     */
    public static void cleanCache() {
        UserCache.remove();
        PersistentCookieStore.getInstance().removeAll();
        TyWebViewCookie.removeCookie();
    }

    /**
     * 初始化数据源
     *
     * @param dataSource 域名
     */
    public static void initNet(String dataSource) {
        NetCache.save(new Net(dataSource));
    }

    /**
     * 清理数据源
     */
    public static void cleanNet() {
        RetrofitFactory.clear();
    }

    /**
     * 重新初始化视图
     */
    public static void cleanFragment(){
        TyFragmentManager.reset();
    }

}
