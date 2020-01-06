package com.abc.lib_cache.urlStrategy.parser;

import com.abc.lib_cache.config.ProxyConstant;
import com.abc.lib_cache.model.UrlInfo;

/**
 * author       : frog
 * time         : 2019-09-26 14:49
 * desc         : 重定向
 * version      : 1.0.0
 */
public class RedirectHandler extends ParserHandler {

    private static final String SUFFIX = "redirect";

    @Override
    protected boolean isHandler(UrlInfo urlInfo) {
        return urlInfo.getUrl().endsWith(SUFFIX);
    }

    @Override
    protected void parser(UrlInfo urlInfo) {
        urlInfo.setSuc(true);
    }

    @Override
    protected String getType() {
        return ProxyConstant.ResType.REDIRECT;
    }

}
