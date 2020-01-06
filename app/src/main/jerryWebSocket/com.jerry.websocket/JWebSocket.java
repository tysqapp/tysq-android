package com.jerry.websocket;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author       : frog
 * time         : 2019-09-11 14:53
 * desc         : WebSocket 入口
 * version      : 1.4.0
 */
public class JWebSocket {

    public static final String TAG = "JWebSocketListener";

    private static volatile JWebSocket instance = null;

    private final MyWebSocketManager mWebSocketManager;
    private final Map<Integer, List<JWebSocketDataListener>> mSubscribeListeners;

    public final Gson mGson;

    private JWebSocket() {
        mGson = new Gson();
        mSubscribeListeners = new HashMap<>();
        mWebSocketManager = new MyWebSocketManager(mSubscribeListeners);
    }

    public static JWebSocket getDefault() {

        if (instance == null) {
            synchronized (JWebSocket.class) {
                if (instance == null) {
                    instance = new JWebSocket();
                }
            }
        }

        return instance;

    }

    /**
     * 注册
     *
     * @param type     类型
     * @param listener 监听器
     */
    public JWebSocket register(int type, JWebSocketDataListener listener) {
        List<JWebSocketDataListener> listeners = mSubscribeListeners.get(type);
        if (listeners == null) {
            listeners = new ArrayList<>();
            mSubscribeListeners.put(type, listeners);
        }

        listeners.add(listener);

        return this;
    }

    /**
     * 注销
     *
     * @param listener 监听器
     */
    public void unregister(JWebSocketDataListener listener) {
        for (Map.Entry<Integer, List<JWebSocketDataListener>> entry
                : mSubscribeListeners.entrySet()) {

            List<JWebSocketDataListener> value = entry.getValue();
            value.remove(listener);
        }
    }

    /**
     * 发送信息
     */
    public void send(Object obj) {

        String json = mGson.toJson(obj);
        send(json);

    }


    /**
     * 发送信息
     */
    public void send(String msg) {
        mWebSocketManager.sendMsg(msg);
    }

    /**
     * 释放
     */
    public void release() {
        mWebSocketManager.release();
    }

}
