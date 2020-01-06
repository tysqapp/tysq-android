package com.abc.lib_cache.urlStrategy.parser;

import android.text.TextUtils;

import com.abc.lib_cache.config.ProxyConstant;
import com.abc.lib_cache.model.UrlInfo;
import com.abc.lib_cache.utils.UrlHelper;

/**
 * author       : frog
 * time         : 2019-09-26 11:00
 * desc         : TS 的 解析规则
 * version      : 1.0.0
 * <p>
 * https://127.0.0.1:9090/api/v1/files/1589/[文件名]/240p00000.ts
 */
public class TsHandler extends ParserHandler {

    private final static String TS = ".ts";
    private static final String P = "p";

    @Override
    protected boolean isHandler(UrlInfo urlInfo) {
        return urlInfo.getUrl().endsWith(TS);
    }

    @Override
    protected void parser(UrlInfo urlInfo) {

        // ["240", "00000"]
        String lastPathSegment = urlInfo.getUri().getLastPathSegment();
        if (lastPathSegment == null) {
            return;
        }
        String[] split = lastPathSegment.split(P);
        if (split.length < 2) {
            return;
        }

        // MD5("/api/v1/files/1589/[文件名]/")
        String folderName = UrlHelper.getFolderName(urlInfo.getUri());
        if(TextUtils.isEmpty(folderName)){
            return;
        }

        // 00000
        String fileName = UrlHelper.getFileName(split[1]);
        if(TextUtils.isEmpty(fileName)){
            return;
        }

        // 240
        String rateCode = split[0];
        if(TextUtils.isEmpty(rateCode)){
            return;
        }

        urlInfo.setFolderName(folderName);
        urlInfo.setRateCode(rateCode);
        urlInfo.setFileName(fileName);
        urlInfo.setSuc(true);

    }

    @Override
    protected String getType() {
        return ProxyConstant.ResType.TS;
    }

}
