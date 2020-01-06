package com.abc.lib_websocket.manager;

import okio.ByteString;

/**
 * @author
 * @date 创建时间：2018/12/14
 * @description 状态回调
 */
public interface WebSocketManager {

    void onConnectSuc();

    void onConnectFailure(boolean reconnect);

    void onMessage(String msg);

    void onMessage(ByteString msg);

    void sendMsg(String msg);

    void release();

}
