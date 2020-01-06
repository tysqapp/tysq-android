package com.jerry.websocket.listener;

import com.jerry.websocket.model.JWebSocketModel;

/**
 * author       : frog
 * time         : 2019-09-11 16:24
 * desc         : WebSocket 的 监听器
 * version      : 1.4.0
 */
public interface JWebSocketDataListener {

    void onReceiveWebSocketData(JWebSocketModel model);

}
