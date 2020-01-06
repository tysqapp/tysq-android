package com.abc.lib_websocket.websocket;

import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * author       : frog
 * time         : 2018-12-14 18:04
 * email        : xxxxx
 * desc         : WebSocket的接口，BaseWebSocketManager中会通过这个接口，操作WebSocket
 * version      : 1.0.0
 */

public abstract class JWebSocketListener extends WebSocketListener {

    /**
     * 获取当前的状态
     */
    public abstract int getCurState();

    /**
     * 设置当前的状态
     */
    public abstract void setCurState(int curState);

    /**
     * 发送订阅
     *
     * @param msg 订阅信息
     */
    public abstract void sendMsg(String msg);

    /**
     * 释放
     */
    public abstract void release();

    /**
     * 复位
     */
    public abstract void reset();

    /**
     * 测试使用
     */
    public abstract WebSocket _getWebSocket();

}
