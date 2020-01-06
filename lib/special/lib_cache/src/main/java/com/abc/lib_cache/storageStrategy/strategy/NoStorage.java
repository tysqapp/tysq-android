package com.abc.lib_cache.storageStrategy.strategy;

import com.abc.lib_cache.message.RequestMessage;
import com.abc.lib_cache.storageStrategy.StrategyBase;
import com.abc.lib_cache.urlStrategy.IUrlInfo;
import com.abc.lib_log.JLogUtils;

import java.io.OutputStream;

import okhttp3.Response;

/**
 * author       : frog
 * time         : 2019-09-26 15:46
 * desc         : 不存储
 * version      : 1.0.0
 */
public class NoStorage extends StrategyBase {

    public NoStorage(IUrlInfo urlInfo,
                     RequestMessage requestMessage,
                     OutputStream outputStream,
                     JLogUtils log) {
        super(urlInfo, requestMessage, outputStream, log);
    }

    @Override
    public boolean run() {
        Response response   = requestOriginal(requestMessage, urlInfo.getUrlInfo());

        if(response == null){
            return false;
        }

        transmitStream(response, outputStream);

        response.close();

        return true;
    }

}
