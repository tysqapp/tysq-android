package com.abc.lib_cache.storageStrategy;

import android.text.TextUtils;

import com.abc.lib_cache.config.ProxyConfig;
import com.abc.lib_cache.config.ProxyConstant;
import com.abc.lib_cache.lru.DiskLruCache;
import com.abc.lib_cache.lru.LruUtils;
import com.abc.lib_cache.message.RequestMessage;
import com.abc.lib_cache.model.UrlInfo;
import com.abc.lib_cache.okhttp.OkHttpHelper;
import com.abc.lib_cache.urlStrategy.IUrlInfo;
import com.abc.lib_cache.utils.HostInfo;
import com.abc.lib_log.JLogUtils;
import com.abc.lib_utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * author       : frog
 * time         : 2019-09-26 15:49
 * desc         : 策略基类
 * version      : 1.0.0
 */
public abstract class StrategyBase implements IStorage {

    protected final IUrlInfo urlInfo;
    protected final RequestMessage requestMessage;
    protected final OutputStream outputStream;
    protected final JLogUtils log;

    public StrategyBase(IUrlInfo urlInfo,
                        RequestMessage requestMessage,
                        OutputStream outputStream,
                        JLogUtils log) {
        this.urlInfo = urlInfo;
        this.requestMessage = requestMessage;
        this.outputStream = outputStream;
        this.log = log;
    }

    /**
     * 将远程流转给本地
     *
     * @param response     返回的response
     * @param outputStream 输出流
     */
    protected void transmitStream(Response response,
                                  OutputStream outputStream) {
        String message = response.message();

        try {
            outputStream.write(message.getBytes());

            for (Map.Entry<String, List<String>> entry : response.headers().toMultimap().entrySet()) {

                for (String value : entry.getValue()) {
                    outputStream.write(entry.getKey().getBytes());
                    outputStream.write(":".getBytes());
                    outputStream.write(value.getBytes());
                    outputStream.write(ProxyConstant.MESSAGE.CRLF.getBytes());
                }

            }

            outputStream.write(ProxyConstant.MESSAGE.CRLF.getBytes());

            if (response.body() != null) {
                FileUtils.transmitStream(response.body().byteStream(),
                        outputStream,
                        false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进行请求原始
     *
     * @param requestMessage 请求报文
     * @param urlInfo        url解析
     */
    protected Response requestOriginal(RequestMessage requestMessage, UrlInfo urlInfo) {
        // 进行网络请求
        Request.Builder builder = new Request.Builder();
        String url = urlInfo.getUrl();

        String host = null;
        String hostKey = HostInfo.getKey(urlInfo.getUri());
        if (!TextUtils.isEmpty(hostKey)) {
            host = HostInfo.getHost(hostKey);
        }

        if (TextUtils.isEmpty(host)) {
            host = ProxyConfig.getInstance().getServerHost();
        }

        url = host + url;
        builder.url(url);

        for (Map.Entry<String, List<String>> entry : requestMessage.getHeader().entrySet()) {
            String key = entry.getKey();

            // 不添加 host
            if (key.equals(ProxyConstant.REQ_HEAD.HOST)) {
                continue;
            }

            List<String> valueList = entry.getValue();

            for (String value : valueList) {
                builder.addHeader(key, value);
            }
        }

        Response response = null;
        try {
            response = OkHttpHelper.getOkHttpInstance()
                    .newCall(builder.build())
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    protected String getFileName() {
        return getFileName(this.urlInfo.getUrlInfo().getRateCode());
    }

    protected String getFileName(String rateCode) {
        return this.urlInfo.getUrlInfo().getFolderName()
                + "-"
                + rateCode
                + "-"
                + this.urlInfo.getUrlInfo().getFileName();
    }

    /**
     * 检测 报文文件 和 内容文件 是否存在
     *
     * @param snapshot LRU 的实体
     * @return 存在返回 true；不存在返回 false
     */
    protected boolean checkFileExist(DiskLruCache.Snapshot snapshot) {

        if (snapshot == null) {
            return false;
        }

        try {
            InputStream msgStream = snapshot.getInputStream(LruUtils.INDEX_MSG);

            if (msgStream == null) {
                return false;
            }

            InputStream bodyStream = snapshot.getInputStream(LruUtils.INDEX_BODY);

            if (bodyStream == null) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

}
