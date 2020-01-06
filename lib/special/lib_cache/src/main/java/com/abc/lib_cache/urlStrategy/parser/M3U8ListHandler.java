package com.abc.lib_cache.urlStrategy.parser;

import android.text.TextUtils;

import com.abc.lib_cache.config.ProxyConstant;
import com.abc.lib_cache.model.UrlInfo;
import com.abc.lib_utils.EncryptionUtils;

/**
 * author       : frog
 * time         : 2019-09-26 11:00
 * desc         : m3u8 索引文件 的 索引
 * version      : 1.0.0
 * <p>
 * https://xxx.tysqapp.com/api/v1/files/1589/[文件名].m3u8
 */
public class M3U8ListHandler extends ParserHandler {

    private static final String SUFFIX = ".m3u8";
    private static final String LIST_INDEX_NAME = "list_index";
    private static final String LIST_INDEX_FOLDER_NAME = "list_index_folder";

    @Override
    protected boolean isHandler(UrlInfo urlInfo) {
        return urlInfo.getUrl().endsWith(SUFFIX);
    }

    @Override
    protected void parser(UrlInfo urlInfo) {

        // /api/v1/files/1589/[文件名].m3u8 替换为 /api/v1/files/1589/[文件名]/
        String path = urlInfo.getUri().getPath();
        if (path == null) {
            return;
        }
        path = path.replace(SUFFIX, "/");
        // MD5(path)
        String folderName = EncryptionUtils.md5(path);
        if (TextUtils.isEmpty(folderName)) {
            return;
        }

        urlInfo.setSuc(true);
        urlInfo.setFolderName(folderName);
        urlInfo.setRateCode(LIST_INDEX_FOLDER_NAME);
        urlInfo.setFileName(LIST_INDEX_NAME);
    }

    @Override
    protected String getType() {
        return ProxyConstant.ResType.M3U8_LIST;
    }
}
