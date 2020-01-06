package com.abc.lib_websocket.websocket;

/**
 * author       : frog
 * time         : 2019-09-11 10:42
 * desc         : WebSocket 的 状态
 * version      : 1.3.0
 */
public interface WebSocketState {

    int NOT_CONNECTED = 1;
    int CONNECTING = 2;
    int CONNECTED = 4;

}
