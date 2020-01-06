package com.jerry.websocket;

import com.jerry.websocket.model.JWebSocketModel;

/**
 * author       : frog
 * time         : 2019-09-11 16:24
 * desc         : WebSocket 数据回调接口
 * version      : 1.4.0
 */
public interface JWebSocketDataListener {

    void onReceiveWebSocketData(JWebSocketModel model);

}
