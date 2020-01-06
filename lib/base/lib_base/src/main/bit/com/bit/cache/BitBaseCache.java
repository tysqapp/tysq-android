package com.bit.cache;

import android.content.Context;
import android.text.TextUtils;

import java.util.Set;

/**
 * author       : frog
 * time         : 2019/3/27 下午4:29
 * desc         : 缓存工具类
 * version      : 1.3.0
 */
public abstract class BitBaseCache {

    private CacheWrapper mCache;

    /**
     * 上下文
     */
    protected static Context APP_CONTEXT;

    protected BitBaseCache() {
        this(null);
    }

    protected BitBaseCache(String name) {

        if (APP_CONTEXT == null) {
            throw new RuntimeException("BitBaseCache not init in Application");
        }

        if (mCache == null) {

            String spName = name == null ? getSpName() : name;

            if (TextUtils.isEmpty(spName)) {
                throw new RuntimeException("The cache name can't be empty!!");
            }

            mCache = new CacheWrapper(
                    APP_CONTEXT.getSharedPreferences(spName, Context.MODE_PRIVATE));
        }
    }

    /**
     * 在 Application 中需要进行初始化
     *
     * @param context 建议为 {@link android.app.Application}
     */
    public static void init(Context context) {
        APP_CONTEXT = context;
    }

    /**
     * SharedPreferences存储在sd卡中的文件名字
     */
    protected abstract String getSpName();

    //========================================设置 值 start=========================================

    /**
     * 保存String类型的值，不限定有效时间
     *
     * @param key   键
     * @param value 值
     */
    public void setCache(String key, String value) {
        mCache.setCache(key, value);
    }

    /**
     * 保存String类型的值，限定有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间，单位为毫秒（需大于0，否则无效）
     */
    public void setCache(String key, String value, long time) {
        mCache.setCache(key, value, time);
    }

    /**
     * 保存boolean类型的值，不限定有效时间
     *
     * @param key   键
     * @param value 值
     */
    public void setCache(String key, boolean value) {
        mCache.setCache(key, value);
    }

    /**
     * 保存boolean类型的值，限定有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间，单位为毫秒（需大于0，否则无效）
     */
    public void setCache(String key, boolean value, long time) {
        mCache.setCache(key, value, time);
    }

    /**
     * 保存float类型的值，不限定有效时间
     *
     * @param key   键
     * @param value 值
     */
    public void setCache(String key, float value) {
        mCache.setCache(key, value);
    }

    /**
     * 保存float类型的值，限定有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间，单位为毫秒（需大于0，否则无效）
     */
    public void setCache(String key, float value, long time) {
        mCache.setCache(key, value, time);
    }

    /**
     * 保存long类型的值，不限定有效时间
     *
     * @param key   键
     * @param value 值
     */
    public void setCache(String key, long value) {
        mCache.setCache(key, value);
    }

    /**
     * 保存long类型的值，限定有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间，单位为毫秒（需大于0，否则无效）
     */
    public void setCache(String key, long value, long time) {
        mCache.setCache(key, value, time);
    }

    /**
     * 保存int类型的值，不限定有效时间
     *
     * @param key   键
     * @param value 值
     */
    public void setCache(String key, int value) {
        mCache.setCache(key, value);
    }

    /**
     * 保存int类型的值，限定有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间，单位为毫秒（需大于0，否则无效）
     */
    public void setCache(String key, int value, long time) {
        mCache.setCache(key, value, time);
    }

    /**
     * 保存Set<String>类型的值，不限定有效时间
     *
     * @param key   键
     * @param value 值
     */
    public void setCache(String key, Set<String> value) {
        mCache.setCache(key, value);
    }

    /**
     * 保存Set<String>类型的值，限定有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间，单位为毫秒（需大于0，否则无效）
     */
    public void setCache(String key, Set<String> value, long time) {
        mCache.setCache(key, value, time);
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
        return mCache.getBoolean(key);
    }

    /**
     * 获取 key值 对应的 内容
     *
     * @param key 键
     * @return 如果有值，且在时间内或是不限制，则返回对应的内容；否则返回 {@link CacheWrapper#DEFAULT_NUM}
     */
    public float getFloat(String key) {
        return mCache.getFloat(key);
    }

    /**
     * 获取 key值 对应的 内容
     *
     * @param key 键
     * @return 如果有值，且在时间内或是不限制，则返回对应的内容；否则返回 {@link CacheWrapper#DEFAULT_NUM}
     */
    public long getLong(String key) {
        return mCache.getLong(key);
    }

    /**
     * 获取 key值 对应的 内容
     *
     * @param key 键
     * @return 如果有值，且在时间内或是不限制，则返回对应的内容；否则返回 {@link CacheWrapper#DEFAULT_NUM}
     */
    public int getInt(String key) {
        return mCache.getInt(key);
    }

    /**
     * 获取 key值 对应的 内容
     *
     * @param key 键
     * @return 如果有值，且在时间内或是不限制，则返回对应的内容；否则返回 null
     */
    public String getString(String key) {
        return mCache.getString(key);
    }

    /**
     * 获取 key值 对应的 内容
     *
     * @param key 键
     * @return 如果有值，且在时间内或是不限制，则返回对应的内容；否则返回 null
     */
    public Set<String> getStringSet(String key) {
        return mCache.getStringSet(key);
    }

    //========================================获取 值 end============================================

    /**
     * 是否已经存有该键（过期的值作为不存在）
     *
     * @param key 键
     * @return true：存在；false：不存在
     */
    public boolean isContains(String key) {
        return mCache.isContains(key);
    }

    /**
     * 是否已经存有该键（忽略有效期）
     *
     * @param key 键
     * @return true：存在；false：不存在
     */
    public boolean isContainsIgnoreTime(String key) {
        return mCache.isContainsIgnoreTime(key);
    }

    /**
     * 清空 SharePreference 内容
     */
    public void clear() {
        mCache.clear();
    }

    /**
     * 移除某个值
     *
     * @param key 键
     */
    public void remove(String key) {
        mCache.remove(key);
    }

}