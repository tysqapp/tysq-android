package com.bit.cache;

import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * author       : frog
 * time         : 2019/3/27 下午4:39
 * desc         : 缓存操作的包装类，用于屏蔽一些操作
 * version      : 1.3.0
 */
public class CacheWrapper {

    private static final String LIMIT = "_limit";
    private static final String DATA = "_data";

    /**
     * 一天
     */
    public static final long DAY = 60 * 60 * 24 * 1000;
    /**
     * 一周
     */
    public static final long WEEK = 7 * 60 * 60 * 24 * 1000;
    /**
     * 一个月（三十天）
     */
    public static final long MONTH = 30 * 60 * 60 * 24 * 1000;

    /**
     * 无有效期
     */
    private static final long NO_LIMIT = -1;
    /**
     * 数值型默认值
     */
    public static final Integer DEFAULT_NUM = -9999;

    public static final Long DEFAULT_LONG_NUM = -9999L;

    private SharedPreferences mCache;

    /**
     * 初始化需要传入一个
     *
     * @param cache 共享内存对象
     */
    public CacheWrapper(SharedPreferences cache) {
        this.mCache = cache;
    }

    //========================================设置 值 start=========================================

    /**
     * 保存String类型的值，不限定有效时间
     *
     * @param key   键
     * @param value 值
     */
    public void setCache(String key, String value) {
        setCache(key, value, NO_LIMIT);
    }

    /**
     * 保存String类型的值，限定有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间，单位为毫秒（需大于0，否则无效）
     */
    public void setCache(String key, String value, long time) {
        SharedPreferences.Editor shareData = mCache.edit();

        if (time > 0) {
            long limitTime = System.currentTimeMillis() + time;
            shareData.putLong(key + LIMIT, limitTime);
        }

        shareData.putString(key + DATA, value);

        SharedPreferencesCompat.apply(shareData);
    }

    /**
     * 保存boolean类型的值，不限定有效时间
     *
     * @param key   键
     * @param value 值
     */
    public void setCache(String key, boolean value) {
        setCache(key, value, NO_LIMIT);
    }

    /**
     * 保存boolean类型的值，限定有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间，单位为毫秒（需大于0，否则无效）
     */
    public void setCache(String key, boolean value, long time) {
        SharedPreferences.Editor shareData = mCache.edit();

        if (time > 0) {
            long limitTime = System.currentTimeMillis() + time;
            shareData.putLong(key + LIMIT, limitTime);
        }

        shareData.putBoolean(key + DATA, value);

        SharedPreferencesCompat.apply(shareData);
    }

    /**
     * 保存float类型的值，不限定有效时间
     *
     * @param key   键
     * @param value 值
     */
    public void setCache(String key, float value) {
        setCache(key, value, NO_LIMIT);
    }

    /**
     * 保存float类型的值，限定有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间，单位为毫秒（需大于0，否则无效）
     */
    public void setCache(String key, float value, long time) {
        SharedPreferences.Editor shareData = mCache.edit();

        if (time > 0) {
            long limitTime = System.currentTimeMillis() + time;
            shareData.putLong(key + LIMIT, limitTime);
        }

        shareData.putFloat(key + DATA, value);

        SharedPreferencesCompat.apply(shareData);
    }

    /**
     * 保存long类型的值，不限定有效时间
     *
     * @param key   键
     * @param value 值
     */
    public void setCache(String key, long value) {
        setCache(key, value, NO_LIMIT);
    }

    /**
     * 保存long类型的值，限定有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间，单位为毫秒（需大于0，否则无效）
     */
    public void setCache(String key, long value, long time) {
        SharedPreferences.Editor shareData = mCache.edit();

        if (time > 0) {
            long limitTime = System.currentTimeMillis() + time;
            shareData.putLong(key + LIMIT, limitTime);
        }

        shareData.putLong(key + DATA, value);

        SharedPreferencesCompat.apply(shareData);
    }

    /**
     * 保存int类型的值，不限定有效时间
     *
     * @param key   键
     * @param value 值
     */
    public void setCache(String key, int value) {
        setCache(key, value, NO_LIMIT);
    }

    /**
     * 保存int类型的值，限定有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间，单位为毫秒（需大于0，否则无效）
     */
    public void setCache(String key, int value, long time) {
        SharedPreferences.Editor shareData = mCache.edit();

        if (time > 0) {
            long limitTime = System.currentTimeMillis() + time;
            shareData.putLong(key + LIMIT, limitTime);
        }

        shareData.putInt(key + DATA, value);

        SharedPreferencesCompat.apply(shareData);
    }

    /**
     * 保存Set<String>类型的值，不限定有效时间
     *
     * @param key   键
     * @param value 值
     */
    public void setCache(String key, Set<String> value) {
        setCache(key, value, NO_LIMIT);
    }

    /**
     * 保存Set<String>类型的值，限定有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间，单位为毫秒（需大于0，否则无效）
     */
    public void setCache(String key, Set<String> value, long time) {
        SharedPreferences.Editor shareData = mCache.edit();

        if (time > 0) {
            long limitTime = System.currentTimeMillis() + time;
            shareData.putLong(key + LIMIT, limitTime);
        }

        shareData.putStringSet(key + DATA, value);

        SharedPreferencesCompat.apply(shareData);
    }

    //========================================设置 值 end===========================================

    //==============================================================================================
    //========================================获取 值 start==========================================
    //==============================================================================================

    /**
     * 获取 key值 对应的 内容
     *
     * @param key 键
     * @return 如果有值，且在时间内或是不限制，则返回对应的内容；否则返回 false
     */
    public boolean getBoolean(String key) {
        long deadline = mCache.getLong(key + LIMIT, NO_LIMIT);

        // 为 NO_LIMIT 说明，不限制则直接返回真正的值
        if (deadline == NO_LIMIT) {
            return mCache.getBoolean(key + DATA, false);
        }

        // 过期则返回false
        if (deadline < System.currentTimeMillis()) {
            return false;
        }

        // 有效期内返回真正的值
        return mCache.getBoolean(key + DATA, false);
    }

    /**
     * 获取 key值 对应的 内容
     *
     * @param key 键
     * @return 如果有值，且在时间内或是不限制，则返回对应的内容；否则返回 {@link #DEFAULT_NUM}
     */
    public float getFloat(String key) {
        long deadline = mCache.getLong(key + LIMIT, NO_LIMIT);

        // 为 NO_LIMIT 说明，不限制则直接返回真正的值
        if (deadline == NO_LIMIT) {
            return mCache.getFloat(key + DATA, DEFAULT_NUM);
        }

        // 过期则返回 {@link #DEFAULT_NUM}
        if (deadline < System.currentTimeMillis()) {
            return DEFAULT_NUM;
        }

        // 有效期内返回真正的值
        return mCache.getFloat(key + DATA, DEFAULT_NUM);
    }

    /**
     * 获取 key值 对应的 内容
     *
     * @param key 键
     * @return 如果有值，且在时间内或是不限制，则返回对应的内容；否则返回 {@link #DEFAULT_NUM}
     */
    public long getLong(String key) {
        long deadline = mCache.getLong(key + LIMIT, NO_LIMIT);

        // 为 NO_LIMIT 说明，不限制则直接返回真正的值
        if (deadline == NO_LIMIT) {
            return mCache.getLong(key + DATA, DEFAULT_LONG_NUM);
        }

        // 过期则返回 {@link #DEFAULT_NUM}
        if (deadline < System.currentTimeMillis()) {
            return DEFAULT_LONG_NUM;
        }

        // 有效期内返回真正的值
        return mCache.getLong(key + DATA, DEFAULT_LONG_NUM);
    }

    /**
     * 获取 key值 对应的 内容
     *
     * @param key 键
     * @return 如果有值，且在时间内或是不限制，则返回对应的内容；否则返回 {@link #DEFAULT_NUM}
     */
    public int getInt(String key) {
        long deadline = mCache.getLong(key + LIMIT, NO_LIMIT);

        // 为 NO_LIMIT 说明，不限制则直接返回真正的值
        if (deadline == NO_LIMIT) {
            return mCache.getInt(key + DATA, DEFAULT_NUM);
        }

        // 过期则返回false
        if (deadline < System.currentTimeMillis()) {
            return DEFAULT_NUM;
        }

        // 有效期内返回真正的值
        return mCache.getInt(key + DATA, DEFAULT_NUM);
    }

    /**
     * 获取 key值 对应的 内容
     *
     * @param key 键
     * @return 如果有值，且在时间内或是不限制，则返回对应的内容；否则返回 null
     */
    public String getString(String key) {
        long deadline = mCache.getLong(key + LIMIT, NO_LIMIT);

        // 为 NO_LIMIT 说明，不限制则直接返回真正的值
        if (deadline == NO_LIMIT) {
            return mCache.getString(key + DATA, null);
        }

        // 过期则返回 null
        if (deadline < System.currentTimeMillis()) {
            return null;
        }

        // 有效期内返回真正的值
        return mCache.getString(key + DATA, null);
    }

    /**
     * 获取 key值 对应的 内容
     *
     * @param key 键
     * @return 如果有值，且在时间内或是不限制，则返回对应的内容；否则返回 null
     */
    public Set<String> getStringSet(String key) {
        long deadline = mCache.getLong(key + LIMIT, NO_LIMIT);

        // 为 NO_LIMIT 说明，不限制则直接返回真正的值
        if (deadline == NO_LIMIT) {
            return mCache.getStringSet(key + DATA, null);
        }

        // 过期则返回 null
        if (deadline < System.currentTimeMillis()) {
            return null;
        }

        // 有效期内返回真正的值
        return mCache.getStringSet(key + DATA, null);
    }

    //========================================获取 值 end============================================

    /**
     * 是否已经存有该键（过期的值作为不存在）
     *
     * @param key 键
     * @return true：存在；false：不存在
     */
    public boolean isContains(String key) {
        long deadline = mCache.getLong(key + LIMIT, NO_LIMIT);

        if (deadline == NO_LIMIT) {
            return mCache.contains(key + DATA);
        }

        if (deadline < System.currentTimeMillis()) {
            return false;
        }

        return mCache.contains(key + DATA);
    }

    /**
     * 是否已经存有该键（忽略有效期）
     *
     * @param key 键
     * @return true：存在；false：不存在
     */
    public boolean isContainsIgnoreTime(String key) {
        return mCache.contains(key + DATA);
    }

    /**
     * 清空 SharePreference 内容
     */
    public void clear() {
        SharedPreferences.Editor editor = mCache.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 移除某个值
     *
     * @param key 键
     */
    public void remove(String key) {
        SharedPreferences.Editor shareData = mCache.edit();
        shareData.remove(key + DATA);
        shareData.remove(key + LIMIT);
        SharedPreferencesCompat.apply(shareData);
    }

    /**
     * author       : frog
     * time         : 2019/3/27 下午5:30
     * desc         : 兼容类
     * version      : 1.3.0
     */
    private static class SharedPreferencesCompat {

        // 避免每次都要进行反射
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException ignored) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException |
                    IllegalAccessException |
                    InvocationTargetException ignored) {
            }

            editor.commit();
        }
    }

}
