package com.abc.lib_cache.storageStrategy.strategy.storage;

import com.abc.lib_cache.config.ProxyConstant;
import com.abc.lib_cache.lru.DiskLruCache;
import com.abc.lib_cache.lru.LruUtils;
import com.abc.lib_cache.message.RequestMessage;
import com.abc.lib_cache.model.UrlInfo;
import com.abc.lib_cache.urlStrategy.IUrlInfo;
import com.abc.lib_log.JLogUtils;

import java.io.OutputStream;

/**
 * author       : frog
 * time         : 2019-09-26 15:46
 * desc         : Ts 文件存储
 * version      : 1.0.0
 */
public class TsStorage extends StorageBase {

    public TsStorage(IUrlInfo urlInfo,
                     RequestMessage requestMessage,
                     OutputStream outputStream,
                     JLogUtils log) {
        super(urlInfo, requestMessage, outputStream, log);
    }

    @Override
    protected DiskLruCache.Snapshot obtainFile() {
        return obtainFile(urlInfo.getUrlInfo(), log);
    }

    /**
     * 获取合适的码率 文件
     *
     * @param urlInfo 路径解析
     * @return 如果成功则返回文件，否则返回null
     */
    private DiskLruCache.Snapshot obtainFile(UrlInfo urlInfo, JLogUtils log) {
        String codeRate = urlInfo.getRateCode();

        DiskLruCache.Snapshot snapshot = null;

        log.title("obtainFile");
        log.add("codeRate").colon().add(codeRate).enterContent();

        for (int i = ProxyConstant.CODE_RATE_LIST.size() - 1; i >= 0; i--) {

            String itemCodeRate = ProxyConstant.CODE_RATE_LIST.get(i);

            String bodyFileName = getFileName(itemCodeRate);
            snapshot = LruUtils.getInstance().getSnapshot(bodyFileName);

            boolean isExist = checkFileExist(snapshot);

            log.add("itemCodeRate").colon().add(itemCodeRate).enterParam();
            log.add("bodyFileName").colon().add(bodyFileName).enterParam();

            if (isExist) {
                log.add("check result").colon().add(" √ ").enterParam();
                break;
            }

            // 如果码率相等则终止继续往下找
            if (codeRate.equals(itemCodeRate)) {
                log.add("Stop to search.It's the min code rate.");
                break;
            }

            log.add("check result").colon().add(" x ").enterParam();
            log.enterContent();

        }

        return snapshot;
    }

}
