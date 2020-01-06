package com.abc.lib_cache.urlStrategy.parser;

import android.text.TextUtils;

import com.abc.lib_cache.config.ProxyConstant;
import com.abc.lib_cache.model.UrlInfo;
import com.abc.lib_cache.utils.UrlHelper;

/**
 * author       : frog
 * time         : 2019-09-26 11:00
 * desc         : m3u8 的 索引文件
 * version      : 1.0.0
 * <p>
 * https://xxx.tysqapp.com/api/v1/files/1589/[文件名]/240p.m3u8
 */
public class M3U8Handler extends ParserHandler {

    private static final String SUFFIX = "p.m3u8";
    private static final String INDEX_NAME = "index";

    @Override
    protected boolean isHandler(UrlInfo urlInfo) {
        return urlInfo.getUrl().endsWith(SUFFIX);
    }

    @Override
    protected void parser(UrlInfo urlInfo) {

        // MD5("/api/v1/files/1589/[文件名]/")
        String folderName = UrlHelper.getFolderName(urlInfo.getUri());
        if(TextUtils.isEmpty(folderName)){
            return;
        }

        // 240
        String lastPathSegment = urlInfo.getUri().getLastPathSegment();
        if (lastPathSegment == null) {
            return;
        }
        String codeRate = lastPathSegment.replace(SUFFIX, "");
        if (TextUtils.isEmpty(codeRate)) {
            return;
        }

        urlInfo.setSuc(true);
        urlInfo.setFileName(INDEX_NAME);
        urlInfo.setFolderName(folderName);
        urlInfo.setRateCode(codeRate);

    }

    @Override
    protected String getType() {
        return ProxyConstant.ResType.M3U8;
    }

}
