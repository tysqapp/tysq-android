package com.tysq.ty_android.local.sp;

import cache.Net;

import com.bit.cache.AppCache;
import com.google.gson.Gson;

import java.lang.String;

/**
 * author       : frog
 * time         : 2019-08-14 15:36
 * desc         : 网络
 * version      : 1.3.0
 */
public final class NetCache {
    private static volatile Net instance;

    private static final String SP_NAME = "net_cache";

    private static final Gson mGson = new Gson();

    public static Net getDefault() {
        if (instance == null) {
            synchronized (Net.class) {
                if (instance == null) {
                    refresh();
                }
            }
        }
        return instance;
    }

    public static void refresh() {
        synchronized (Net.class) {
            String json = AppCache.getInstance().getString(SP_NAME);
            if (json == null || json.equals("")) {
                instance = null;
            } else {
                instance = (Net) mGson.fromJson(json, Net.class);
            }
        }
    }

    public static void remove() {
        AppCache.getInstance().remove(SP_NAME);
        instance = null;
    }

    public static boolean isEmpty() {
        return getDefault() == null;
    }

    public static void save(Net model) {
        instance = model;
        String json = mGson.toJson(model);
        AppCache.getInstance().setCache(SP_NAME, json);
    }
}
