package com.abc.lib_cache.socket;

import android.text.TextUtils;

import com.abc.lib_cache.config.ProxyConfig;
import com.abc.lib_cache.config.ProxyConstant;
import com.abc.lib_cache.httpCode.HttpCodec;
import com.abc.lib_cache.message.RequestMessage;
import com.abc.lib_cache.model.UrlInfo;
import com.abc.lib_cache.storageStrategy.IStorage;
import com.abc.lib_cache.storageStrategy.strategy.NoStorage;
import com.abc.lib_cache.storageStrategy.strategy.RedirectStorage;
import com.abc.lib_cache.storageStrategy.strategy.storage.M3U8ListStorage;
import com.abc.lib_cache.storageStrategy.strategy.storage.M3U8Storage;
import com.abc.lib_cache.storageStrategy.strategy.storage.TsStorage;
import com.abc.lib_cache.urlStrategy.UrlInfoWrapper;
import com.abc.lib_cache.urlStrategy.parser.ParserFactory;
import com.abc.lib_cache.utils.SocketPoolInfo;
import com.abc.lib_log.JLogUtils;
import com.abc.lib_utils.CloseUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;

/**
 * author       : frog
 * time         : 2019-09-25 10:44
 * desc         : 缓存代理socket
 * version      : 1.0.0
 */
public class CacheProxySocket implements Runnable {

    protected static final SimpleDateFormat SDF
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private final String mAddress;

    private final HttpCodec mHttpCodec;
    private final RequestMessage mRequestMessage;

    private Socket mSocket;

    private InputStream mInputStream;
    private OutputStream mOutputStream;

    private JLogUtils mLog;

    public CacheProxySocket(String address) {
        this.mAddress = address;
        this.mRequestMessage = new RequestMessage();
        this.mLog = JLogUtils.getDefault();
        this.mHttpCodec = new HttpCodec(mLog);
    }

    @Override
    public void run() {

        mLog.content("socket running...");
        mLog.content("address: " + mAddress);

        // 检测是否有对应的socket
        mSocket = SocketPoolInfo.getSocket(mAddress);
        if (mSocket == null) {
            mLog.content("The mAddress(" + mAddress + "）is not exist in SOCKET_IN_MAP!!")
                    .showError();
            releaseAll();
            return;
        }

        mLog.content("get socket success...");

        // 获取 输入，输出流
        try {
            mInputStream = new BufferedInputStream(mSocket.getInputStream());
            mOutputStream = new BufferedOutputStream(mSocket.getOutputStream());
        } catch (IOException e) {
            if (ProxyConfig.getInstance().isDebug()) {
                e.printStackTrace();
            }
            mLog.showError();
            releaseAll();
            return;
        }

        mLog.content("get socket stream success...");

        // 解析报文
        try {
            handleMessage();
        } catch (IOException e) {
            if (ProxyConfig.getInstance().isDebug()) {
                e.printStackTrace();
            }
            mLog.showError();
            releaseAll();
            return;
        }

        mLog.content("parser message success...");

        mRequestMessage.showLog(mLog);

        String url = mRequestMessage.getUrl();
        if (TextUtils.isEmpty(url)) {
            mLog.content("URL 为空").showError();
            releaseAll();
            return;
        }

        UrlInfo urlInfo = new UrlInfo(url);
        UrlInfoWrapper urlInfoWrapper
                = new UrlInfoWrapper(ParserFactory.getUrlParser().handleMessage(urlInfo));

        mLog.content("File info");
        urlInfo.show(mLog);

        // 解析不成功
        if (!urlInfoWrapper.isSuc()) {
            mLog.content("url info 解析错误");
            urlInfo.setType(ProxyConstant.ResType.ERROR);
        }

        IStorage storage;
        switch (urlInfo.getType()) {
            case ProxyConstant.ResType.M3U8_LIST:
                storage = new M3U8ListStorage(urlInfoWrapper, mRequestMessage, mOutputStream, mLog);
                break;
            case ProxyConstant.ResType.M3U8:
                storage = new M3U8Storage(urlInfoWrapper, mRequestMessage, mOutputStream, mLog);
                break;
            case ProxyConstant.ResType.TS:
                storage = new TsStorage(urlInfoWrapper, mRequestMessage, mOutputStream, mLog);
                break;
            case ProxyConstant.ResType.REDIRECT:
                storage = new RedirectStorage(urlInfoWrapper, mRequestMessage, mOutputStream, mLog);
                break;
            default:
                storage = new NoStorage(urlInfoWrapper, mRequestMessage, mOutputStream, mLog);
                break;
        }

        boolean isSuc = storage.run();

        mLog.showInfo();

        releaseAll();

    }

    /**
     * 处理报文
     */
    private void handleMessage() throws IOException {
        mHttpCodec.readStateLine(mInputStream, mRequestMessage);
        mHttpCodec.readHeaders(mInputStream, mRequestMessage);
    }

    /**
     * 释放资源
     * 关闭各种流 和 关闭socket
     */
    private void releaseAll() {

        CloseUtils.close(mInputStream);
        CloseUtils.close(mOutputStream);
        CloseUtils.close(mSocket);

        SocketPoolInfo.removeSocket(mAddress);

    }

}
