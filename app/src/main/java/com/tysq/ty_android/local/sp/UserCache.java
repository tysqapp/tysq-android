package com.tysq.ty_android.local.sp;

import com.bit.cache.AppCache;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import cache.User;

/**
 * author       : frog
 * time         : 2019/4/11 下午2:27
 * desc         : 用户信息
 * version      : 1.3.0
 */
public final class UserCache {
    private static volatile User instance;

    private static final String SP_NAME = "user_cache";

    private static final Gson mGson = new Gson();

    public static User getDefault() {
        if (instance == null) {
            synchronized (User.class) {
                if (instance == null) {
                    refresh();
                }
            }
        }
        return instance;
    }

    public static void refresh() {
        synchronized (User.class) {
            String json = AppCache.getInstance().getString(SP_NAME);
            if (json == null || json.equals("")) {
                instance = null;
            } else {
                instance = (User) mGson.fromJson(json, User.class);
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

    public static void save(User model) {

        if (model != null) {
            Crashlytics.setUserIdentifier(model.getAccountId() + "");
        }

        instance = model;
        String json = mGson.toJson(model);
        AppCache.getInstance().setCache(SP_NAME, json);
    }
}
