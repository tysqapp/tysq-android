package com.abc.lib_websocket.manager;

import com.abc.lib_websocket.websocket.JWebSocketListener;
import com.abc.lib_websocket.websocket.WebSocketState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * author       : frog
 * time         : 2019-09-11 15:54
 * desc         : 带有消息缓存机制的 WebSocket
 * version      : 1.3.0
 */
public abstract class CacheWebSocketManager extends BaseWebSocketManager {

    private final List<String> mCacheList = new ArrayList<>();

    @Override
    public void sendMsg(String msg) {
        // 如果已经连接，则进行发送订阅消息，
        // 如果未连接，则将消息先添加至 mCacheList 集合，然后进行连接，等待连接成功后发送订阅
        if (mWebSocket.getCurState() == WebSocketState.CONNECTED) {
            mWebSocket.sendMsg(msg);
        } else {
            synchronized (mLock) {
                // 先加进等待集合，等待连接成功在发送
                mCacheList.add(msg);
            }

            // 进行连接
            connect();
        }
    }

    @Override
    public void onConnectSuc() {
        super.onConnectSuc();
        resend();
    }

    @Override
    protected void scheduleHandle(JWebSocketListener WebSocket) {
        resend();
    }

    private void resend() {
        synchronized (mLock) {

            if (mCacheList.isEmpty() || mWebSocket == null) {
                return;
            }

            Iterator<String> iterator = mCacheList.iterator();
            while (iterator.hasNext()) {

                String msg = iterator.next();
                mWebSocket.sendMsg(msg);

                iterator.remove();

            }

        }
    }

    @Override
    public void release() {
        super.release();
        synchronized (mLock) {
            mCacheList.clear();
        }
    }
}
