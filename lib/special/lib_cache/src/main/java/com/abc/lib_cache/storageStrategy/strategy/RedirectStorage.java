package com.abc.lib_cache.storageStrategy.strategy;

import android.net.Uri;
import android.text.TextUtils;

import com.abc.lib_cache.config.ProxyConfig;
import com.abc.lib_cache.config.ProxyConstant;
import com.abc.lib_cache.message.RequestMessage;
import com.abc.lib_cache.storageStrategy.StrategyBase;
import com.abc.lib_cache.urlStrategy.IUrlInfo;
import com.abc.lib_cache.utils.HttpHelper;
import com.abc.lib_log.JLogUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * author       : frog
 * time         : 2019-09-26 15:46
 * desc         : 重定向
 * version      : 1.0.0
 */
public class RedirectStorage extends StrategyBase {

    public RedirectStorage(IUrlInfo urlInfo,
                           RequestMessage requestMessage,
                           OutputStream outputStream,
                           JLogUtils log) {
        super(urlInfo, requestMessage, outputStream, log);
    }

    @Override
    public boolean run() {
        Response response = requestOriginal(requestMessage, urlInfo.getUrlInfo());

        if (response == null) {
            return false;
        }

        transmitStream(response, outputStream);
        response.close();

        return true;
    }

    /**
     * 将远程流转给本地
     *
     * @param response     返回的response
     * @param outputStream 输出流
     */
    protected void transmitStream(Response response,
                                  OutputStream outputStream) {

        String status = HttpHelper.getStatus(response);
        log.content(status);

        String host = null;

        try {
            outputStream.write(status.getBytes());

            outputStream.write(ProxyConstant.MESSAGE.CRLF.getBytes());
            log.enterContent();

            int bodyLength = -1;
            String resultBody = null;
            if (response.body() != null) {
                ResponseBody resBody = response.body();

                resultBody = resBody.string();

                String location = response.header(ProxyConstant.RESP_HEAD.LOCATION);
                if (!TextUtils.isEmpty(location)) {
                    Uri uri = Uri.parse(location);
                    host = uri.getHost();
                }

                if (!TextUtils.isEmpty(host)) {
                    String localHost = ProxyConfig.getInstance().getHost();

                    String realHostWithHttp = ProxyConstant.SCHEMA_TYPE.HTTP + "://" + host;
                    String realHostWithHttps = ProxyConstant.SCHEMA_TYPE.HTTPS + "://" + host;

                    String resultHost = null;

                    if (resultBody.startsWith(realHostWithHttp)) {
                        resultBody = resultBody.replace(realHostWithHttp, localHost);
                        resultHost = realHostWithHttp;
                    } else if (resultBody.startsWith(realHostWithHttps)) {
                        resultBody = resultBody.replace(realHostWithHttps, localHost);
                        resultHost = realHostWithHttps;
                    }

                    // TODO: 2019-10-29  
//                    if (!TextUtils.isEmpty(resultHost)) {
//                        requestMessage.get
//                        HostInfo.addHost();
//                    }

                }

                bodyLength = resultBody.length();
            }

            for (Map.Entry<String, List<String>> entry : response.headers().toMultimap().entrySet()) {

                for (String value : entry.getValue()) {

                    String key = entry.getKey();

                    if (key.equals(ProxyConstant.RESP_HEAD.LOCATION)) {
                        Uri uri = Uri.parse(value);
                        host = uri.getHost();

                        if (host != null) {
                            int index = value.indexOf(host);
                            value = value.substring(index + host.length());
                        }

                        String localHost = ProxyConfig.getInstance().getHost();
                        value = localHost + value;

                    }

                    if (key.equalsIgnoreCase(ProxyConstant.RESP_HEAD.CONTENT_LENGTH)) {
                        if (bodyLength != -1) {
                            value = bodyLength + "";
                        }
                    }

                    outputStream.write(key.getBytes());
                    outputStream.write(":".getBytes());
                    outputStream.write(value.getBytes());
                    outputStream.write(ProxyConstant.MESSAGE.CRLF.getBytes());

                    log.add(key).colon().add(value).enterParam();
                }

            }

            if (resultBody != null) {
                outputStream.write(resultBody.getBytes());
                log.enterContent().content(resultBody).enterContent();
            }
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
