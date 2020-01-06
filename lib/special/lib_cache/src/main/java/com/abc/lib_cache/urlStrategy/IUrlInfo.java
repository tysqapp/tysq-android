package com.abc.lib_cache.urlStrategy;

import com.abc.lib_cache.model.UrlInfo;

/**
 * author       : frog
 * time         : 2019-09-26 10:09
 * desc         : url 解析接口
 * version      : 1.0.0
 */
public interface IUrlInfo {

    /**
     * 是否成功
     */
    boolean isSuc();

    /**
     * 获取 urlInfo
     */
    UrlInfo getUrlInfo();

}
