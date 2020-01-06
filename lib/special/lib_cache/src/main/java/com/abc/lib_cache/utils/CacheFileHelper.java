package com.abc.lib_cache.utils;

import com.abc.lib_cache.config.ProxyConfig;
import com.abc.lib_utils.FileUtils;

import java.io.File;

/**
 * author       : frog
 * time         : 2019-09-23 16:52
 * desc         : 缓存文件工具
 * version      : 1.0.0
 */
public class CacheFileHelper extends FileUtils {

    private static final String CACHE_FOLDER = "JCacheProxy";

    /**
     * 获取保存文件夹
     */
    public static File getSaveFolder() {
        // /data/data/< package name >/cache/
        File cacheDir = ProxyConfig.getInstance().getContext().getCacheDir();
        return createFolder(cacheDir, CACHE_FOLDER);
    }

    /**
     * 创建对应的 ts 文件
     *
     * @param folderName 文件夹名
     * @param tsFileName ts文件
     * @return 创建成功则返回file，否则返回null
     */
    public static File createFile(String folderName, String tsFileName) {

        File tsFolder = new File(getSaveFolder(), folderName);

        boolean isSuc = createFolder(tsFolder);
        if (!isSuc) {
            return null;
        }

        return createFile(tsFolder, tsFileName);

    }

}
