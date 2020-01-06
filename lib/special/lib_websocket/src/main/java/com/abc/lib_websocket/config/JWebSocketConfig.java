package com.abc.lib_websocket.config;

/**
 * author       : frog
 * time         : 2019-09-10 14:51
 * email        : xxxxx
 * desc         : WebSocket 的 配置
 * version      : 1.0.0
 */
public class JWebSocketConfig {

    private static volatile JWebSocketConfig instance = null;

    private JWebSocketConfig() {
        heartbeat = 10;
    }

    public static JWebSocketConfig getInstance() {

        if (instance == null) {
            synchronized (JWebSocketConfig.class) {
                if (instance == null) {
                    instance = new JWebSocketConfig();
                }
            }
        }

        return instance;

    }

    // 心跳时间，默认为10秒
    private int heartbeat;

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }
}
