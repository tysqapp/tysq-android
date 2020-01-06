package com.abc.lib_multi_download.downloadStrategy;

import com.abc.lib_multi_download.constants.ReqHead;
import com.abc.lib_multi_download.exception.DownloadException;
import com.abc.lib_multi_download.model.DownloadInfo;
import com.abc.lib_multi_download.utils.NetUtils;
import com.abc.lib_utils.CloseUtils;

import java.util.HashMap;

/**
 * author       : frog
 * time         : 2019-10-14 11:07
 * desc         : 续传策略
 * 进行步骤：
 * 1、通过 last-modified 查看是否资源过期
 * 2、如果过期，则删除原有的资源，重新开始
 * 3、如果未过期，则进行下一步
 * version      : 1.0.0
 */
public class ContinueStrategy extends BaseStrategy {

    public ContinueStrategy(DownloadInfo downloadInfo) {
        super(downloadInfo);
    }

    @Override
    public boolean run() throws DownloadException {

        HashMap<String, String> headMap = new HashMap<>();
        headMap.put(ReqHead.IF_MODIFIED_SINCE, mDownloadInfo.getLastModified());

        mResponse = NetUtils.request(mDownloadInfo.getUrl(), headMap);

        if (mResponse == null) {
            if (!isRunning) {
                return false;
            }
            throw new DownloadException("服务器异常", "Response is null!!");
        }

        // 说明之前的资源没变动
        if (mResponse.code() == 304) {
            CloseUtils.close(mResponse);
            return true;
        }

        CloseUtils.close(mResponse);
        if (!isRunning) {
            return false;
        }
        throw new DownloadException("服务器异常", "[Continue Strategy]Response code is " + mResponse.code());

    }

}
