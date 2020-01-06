package com.abc.lib_websocket.websocket;

import com.abc.lib_log.JLogUtils;
import com.abc.lib_websocket.manager.WebSocketManager;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

/**
 * author       : frog
 * time         : 2018-12-14 14:56
 * email        : xxxxx
 * desc         : WebSocket 的 监听基类
 * version      : 1.0.0
 */

public class JWebSocketListenerImpl extends JWebSocketListener {

    // 是否需要重连
    private boolean mReconnect = true;

    // 当前的状态
    private volatile int mCurState = WebSocketState.NOT_CONNECTED;
    // 回调的监听器
    private final WebSocketManager mListener;

    private WebSocket mWebSocket;

    public JWebSocketListenerImpl(WebSocketManager mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onOpen(WebSocket webSocket,
                       Response response) {

        mWebSocket = webSocket;
        setCurState(WebSocketState.CONNECTED);

        if (mListener != null) {
            mListener.onConnectSuc();
        }

    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        if (mListener != null) {
            mListener.onMessage(text);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        if (mListener != null) {
            mListener.onMessage(bytes);
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        JLogUtils.getDefault().title("onClosing")
                .enterContent()
                .content("[ code: " + code + "; msg: " + reason + " ]")
                .showError();
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        JLogUtils.getDefault().title("onClosing")
                .enterContent()
                .content("[ code: " + code + "; msg: " + reason + " ]")
                .showError();
        close();
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        JLogUtils.getDefault().title("onFailure")
                .enterContent()
                .content("[ msg: " + t.getMessage() + " ]")
                .showError();
        close();
    }

    /**
     * 获取当前 WebSocket 的状态
     */
    @Override
    public synchronized int getCurState() {
        return mCurState;
    }

    /**
     * 设置当前 WebSocket 的状态
     *
     * @param curState 当前状态
     *                 {@link WebSocketState#NOT_CONNECTED} 未连接
     *                 {@link WebSocketState#CONNECTING}    连接中
     *                 {@link WebSocketState#CONNECTED}     连接完毕
     */
    @Override
    public synchronized void setCurState(int curState) {
        this.mCurState = curState;
    }

    @Override
    public void sendMsg(String msg) {
        if (mWebSocket != null) {
            mWebSocket.send(msg);
        }
    }

    @Override
    public void release() {
        mReconnect = false;
        if (mWebSocket != null) {
            mWebSocket.cancel();
        }
    }

    @Override
    public void reset() {
        mReconnect = true;
    }

    @Override
    public WebSocket _getWebSocket() {
        return mWebSocket;
    }

    /**
     * 关闭状态
     */
    private void close() {
        // 置空，释放
        mWebSocket = null;

        // 设置成未连接
        setCurState(WebSocketState.NOT_CONNECTED);

        // 失败回调
        if (mListener != null) {
            mListener.onConnectFailure(mReconnect);
        }
    }

}
