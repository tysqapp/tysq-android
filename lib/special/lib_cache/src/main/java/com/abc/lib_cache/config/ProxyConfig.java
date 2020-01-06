package com.abc.lib_cache.config;

import android.content.Context;

/**
 * author       : frog
 * time         : 2019-09-20 17:41
 * desc         : 代理配置
 * version      : 1.0.0
 */
public class ProxyConfig {

    private static final String HTTP = "http://";
    private static final String COLON = ":";

    private static volatile ProxyConfig instance = null;

    private boolean isDebug = true;

    private Context context;
    private String folder;
    private int bufferSize;
    private String host;
    private int port;

    // 服务器的域名
    private String serverHost;

    private ProxyConfig() {

        // 4K
        bufferSize = 4 * 1024;

        folder = "JCacheProxy";

    }

    public static ProxyConfig getInstance() {

        if (instance == null) {
            synchronized (ProxyConfig.class) {
                if (instance == null) {
                    instance = new ProxyConfig();
                }
            }
        }

        return instance;

    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public void init(Context context) {
        this.context = context;
    }

    public ProxyConfig install(String host, int port) {
        this.host = host;
        this.port = port;
        return this;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * 获取域名
     *
     * @return http://127.0.0.1:00000
     */
    public String getHost() {
        return HTTP + host + COLON + port;
    }

    public String getFolder() {
        return folder;
    }

    public ProxyConfig setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public Context getContext() {
        return context;
    }
}
