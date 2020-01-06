package com.abc.lib_multi_download.utils;

/**
 * author       : frog
 * time         : 2019-12-02 10:30
 * desc         : 位运算的工具类
 * version      : 1.0.0
 */
public class BitUtils {

    /**
     * 将 bit1 和 bit2 进行合并返回
     */
    public static int add(int bit1, int bit2) {
        return bit1 | bit2;
    }

    /**
     * 从 original 中移除 remove
     */
    public static int remove(int original, int remove) {
        return original & ~remove;
    }

    /**
     * original 中是否含有 condition
     */
    public static boolean isContain(int original, int condition) {
        return (original & condition) == condition;
    }

}
