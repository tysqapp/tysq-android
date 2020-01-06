package com.abc.lib_cache.lru;

import com.abc.lib_cache.BuildConfig;
import com.abc.lib_cache.utils.CacheFileHelper;
import com.abc.lib_utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * author       : frog
 * time         : 2019-10-09 11:11
 * desc         : lru工具类
 * version      : 1.0.0
 */
public class LruUtils {

    private static DiskLruCache diskLruCache;

    // 报文
    public static final int INDEX_MSG = 0;
    // 内容
    public static final int INDEX_BODY = 1;

    private static final File directory = CacheFileHelper.getSaveFolder();
    // app的版本
    private static final int appVersion = BuildConfig.VERSION_CODE;
    // 每个都需要两个，第一个为报文，第二个为内容
    private final int valueCount;
    // 4G
    private final long maxSize;

    private static volatile LruUtils instance = null;

    private LruUtils() {
        valueCount = 2;
//        maxSize = 4_294_967_296L;
        maxSize = 4 * 1024 * 1024 * 1024L;
        try {
            diskLruCache = DiskLruCache.open(directory, appVersion, valueCount, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
            diskLruCache = null;
        }
    }

    public static LruUtils getInstance() {

        if (instance == null) {
            synchronized (LruUtils.class) {
                if (instance == null) {
                    instance = new LruUtils();
                }
            }
        }

        return instance;

    }

    public boolean saveToFile(String fileKey, InputStream inputStream, int index) {
        DiskLruCache.Editor editor = null;
        try {
            editor = diskLruCache.edit(fileKey);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (editor == null) {
            return false;
        }

        OutputStream fileOutputStream;
        try {
            fileOutputStream = editor.newOutputStream(index);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        boolean isSuc = FileUtils.transmitStream(inputStream, fileOutputStream, false);

        if (isSuc) {
            try {
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                editor.abort();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return isSuc;

    }

    public DiskLruCache.Snapshot getSnapshot(String key) {

        if (diskLruCache == null) {
            return null;
        }

        DiskLruCache.Snapshot snapshot;

        try {
            snapshot = diskLruCache.get(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return snapshot;
    }

    /**
     * 获取编辑的 LRU editor
     *
     * @param key 需要存储的文件名
     * @return editor
     */
    public DiskLruCache.Editor obtainEditor(String key) {

        if (diskLruCache == null) {
            return null;
        }

        DiskLruCache.Editor editor = null;

        try {
            editor = diskLruCache.edit(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return editor;

    }

}