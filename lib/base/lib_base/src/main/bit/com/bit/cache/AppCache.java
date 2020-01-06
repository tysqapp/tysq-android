package com.bit.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * author       : frog
 * time         : 2019/3/29 下午3:14
 * desc         : 2019/3/27 下午5:55
 * version      : 1.3.0
 */
public class AppCache extends BitBaseCache {

    private static volatile Map<String, AppCache> CACHE_MAP = new HashMap<>();
    private static volatile AppCache instance;

    private AppCache(String name) {
        super(name);
    }

    private AppCache() {
        super(null);
    }

    public static AppCache getInstance(String name) {

        AppCache cache = CACHE_MAP.get(name);

        if (cache == null) {
            synchronized (AppCache.class) {
                cache = CACHE_MAP.get(name);
                if (cache == null) {
                    cache = new AppCache(name);
                    CACHE_MAP.put(name, cache);
                }
            }
        }

        return cache;

    }

    public static AppCache getInstance() {

        if (instance == null) {
            synchronized (AppCache.class) {
                if (instance == null) {
                    instance = new AppCache();
                }
            }
        }

        return instance;

    }

    @Override
    protected String getSpName() {
        return APP_CONTEXT.getPackageName() + "_preferences";
    }

}
