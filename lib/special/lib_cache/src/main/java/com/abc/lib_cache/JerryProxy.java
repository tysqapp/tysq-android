package com.abc.lib_cache;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.abc.lib_cache.config.ProxyConfig;
import com.abc.lib_cache.server.HttpCacheProxyServer;
import com.abc.lib_log.JLogUtils;

/**
 * author       : frog
 * time         : 2019-10-09 16:56
 * desc         :
 * version      :
 */
public class JerryProxy {

    private static volatile JerryProxy instance = null;

    private HttpCacheProxyServer httpCacheProxyServer;

    private JerryProxy() {

    }

    public static JerryProxy getInstance() {

        if (instance == null) {
            synchronized (JerryProxy.class) {
                if (instance == null) {
                    instance = new JerryProxy();
                }
            }
        }

        return instance;

    }

    public void init(Context context) {
        ProxyConfig.getInstance().init(context);
    }

    public void setServerHost(String serverHost) {
        ProxyConfig.getInstance().setServerHost(serverHost);
    }

    public void startServer() {
        if (httpCacheProxyServer != null && httpCacheProxyServer.isRunning()) {
            JLogUtils.showInfoQuickly("Cache Proxy is running!");
            return;
        }

        httpCacheProxyServer = new HttpCacheProxyServer();
        new Thread(httpCacheProxyServer).start();
    }

    public void stopServer() {
        if (httpCacheProxyServer == null) {
            return;
        }

        httpCacheProxyServer.stop();
    }

    public boolean isRunning() {
        if (httpCacheProxyServer == null) {
            return false;
        }

        return httpCacheProxyServer.isRunning();
    }

    public String getProxyUrl(String url) {
        return getProxyUrl(Uri.parse(url));
    }

    //    /**
//     * https://xxx.tysqapp.com/api/file/commons/990/[文件]/1080p00000.ts
//     */
//    public String getProxyUrl(Uri uri) {
//        if (!isRunning()) {
//            return uri.toString();
//        }
//
//        String host = uri.getHost();
//
//        if (host == null) {
//            return uri.toString();
//        }
//        // 加 http、https
//        String scheme = uri.getScheme();
//        if (!TextUtils.isEmpty(scheme)) {
//            host = scheme + "://" + host;
//        } else {
//            host = "http://" + host;
//        }
//
//        String key = HostInfo.getKey(uri);
//
//        if (key != null) {
//            HostInfo.addHost(key, host);
//        }
//
//        String url = uri.toString();
//        // 获取 域名 的下标
//        int hostIndex = url.indexOf(host);
//        // url的后缀  /api/file/commons/990/[文件]/1080p00000.ts
//        String urlSuffix = url.substring(hostIndex + host.length());
//
//        return ProxyConfig.getInstance().getHost() + urlSuffix;
//    }

    public String getProxyUrl(Uri uri) {
        if (!isRunning()) {
            return uri.toString();
        }

        String host = uri.getHost();

        if (host == null) {
            return uri.toString();
        }
        // 加 http、https
        String scheme = uri.getScheme();
        if (!TextUtils.isEmpty(scheme)) {
            host = scheme + "://" + host;
        } else {
            host = "http://" + host;
        }

        JerryProxy.getInstance().setServerHost(host);

        String url = uri.toString();

        int hostIndex = url.indexOf(host);
        String urlSuffix = url.substring(hostIndex + host.length());

        return ProxyConfig.getInstance().getHost() + urlSuffix;

    }
}
