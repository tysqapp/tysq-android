package com.abc.lib_cache.urlStrategy;

import com.abc.lib_cache.model.UrlInfo;

/**
 * author       : frog
 * time         : 2019-09-26 12:29
 * desc         : url 信息
 * version      : 1.0.0
 */
public class UrlInfoWrapper implements IUrlInfo {

    private final UrlInfo urlInfo;

    public UrlInfoWrapper(UrlInfo urlInfo) {
        this.urlInfo = urlInfo;
    }

    @Override
    public boolean isSuc() {
        return urlInfo.isSuc();
    }

    @Override
    public UrlInfo getUrlInfo() {
        return urlInfo;
    }

}
