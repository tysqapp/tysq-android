package com.abc.lib_cache.server;

import com.abc.lib_cache.config.ProxyConfig;
import com.abc.lib_cache.socket.CacheProxySocket;

import java.io.IOException;
import java.net.BindException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * author       : frog
 * time         : 2019-09-20 14:06
 * desc         :
 * version      :
 */
public class HttpCacheProxyServer extends BaseServer {

    private static final String PROXY_HOST = "127.0.0.1";

    public HttpCacheProxyServer() {
        super();
    }

    @Override
    protected void createServerSocket() throws IOException, BindException {
        InetAddress inetAddress = Inet4Address.getByName(PROXY_HOST);

        mServerSocket = new ServerSocket(0, 8, inetAddress);
    }

    @Override
    protected String getType() {
        return "HttpCacheProxy";
    }

    @Override
    protected CacheProxySocket getSocketIn(String address) {
        return new CacheProxySocket(address);
    }

    @Override
    public void setPortToConfig() {
        ProxyConfig.getInstance().install(PROXY_HOST, mServerSocket.getLocalPort());
    }

}
