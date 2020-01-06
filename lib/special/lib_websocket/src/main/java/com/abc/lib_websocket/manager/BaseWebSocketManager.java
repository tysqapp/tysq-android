package com.abc.lib_websocket.manager;

import android.text.TextUtils;

import com.abc.lib_log.JLogUtils;
import com.abc.lib_websocket.websocket.JWebSocketListener;
import com.abc.lib_websocket.websocket.JWebSocketListenerImpl;
import com.abc.lib_websocket.websocket.WebSocketState;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.internal.Util;
import okio.ByteString;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * author       : frog
 * time         : 2018-12-14 14:47
 * email        : xxxxx
 * desc         : WebSocket 管理的基类
 * 包含以下功能：
 * 1、连接
 * 2、重连
 * 3、心跳发送
 * version      : 1.0.0
 */

public abstract class BaseWebSocketManager implements WebSocketManager {

    private static final String TAG = "JWebSocket";

    private static final String THREAD_POOL_NAME = "LoginPingPongThread";

    // 每10秒发一次ping [子类需要自行修改]
    private static final int PING_INTERVAL = 10 * 1000;

    // 通过这个进行操作真正的WebSocket
    protected final JWebSocketListener mWebSocket;

    // 线程池
    private ScheduledExecutorService mExecutor;

    protected final Object mLock = new Object();

    BaseWebSocketManager() {
        mWebSocket = new JWebSocketListenerImpl(this);
    }

    /**
     * 进行WebSocket连接
     */
    protected void connect() {

        if (mWebSocket.getCurState() == WebSocketState.NOT_CONNECTED) {

            synchronized (this) {
                int curState = mWebSocket.getCurState();

                // 未连接
                if (curState == WebSocketState.NOT_CONNECTED) {

                    // 复位
                    mWebSocket.reset();
                    // 设置为连接中
                    mWebSocket.setCurState(WebSocketState.CONNECTING);

                    JLogUtils.getDefault()
                            .title("WebSocket")
                            .param("code: " + curState)
                            .param("url: " + getWebSocketUrl())
                            .enterContent()
                            .content("开始连接.....")
                            .enterContent()
                            .showWarn();

                    Request request = new Request
                            .Builder()
                            .url(getWebSocketUrl())
                            .build();
                    getOkHttpClient().newWebSocket(request, mWebSocket);

                } else {
                    JLogUtils.getDefault()
                            .title("WebSocket")
                            .enterContent()
                            .content("已经处于连接状态")
                            .add("code：")
                            .add(curState)
                            .enterContent()
                            .showWarn();
                }
            }

        }

    }

    @Override
    public void onConnectSuc() {

        // 连接成功进行心跳包配置
        mExecutor = new ScheduledThreadPoolExecutor(1,
                Util.threadFactory(THREAD_POOL_NAME, false));

        mExecutor.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        String pingPongMsg = getPingPongMsg();

                        if (mWebSocket != null) {

                            if (!TextUtils.isEmpty(pingPongMsg)) {

                                JLogUtils.getDefault()
                                        .title("PING-PONG")
                                        .enterContent()
                                        .content("[Listener not null]")
                                        .content(pingPongMsg)
                                        .showInfo();

                                mWebSocket.sendMsg(pingPongMsg);
                            }

                            scheduleHandle(mWebSocket);

                        } else {

                            JLogUtils.getDefault()
                                    .title("PING-PONG")
                                    .enterContent()
                                    .content("[Listener is null]")
                                    .content(pingPongMsg)
                                    .showError();
                        }
                    }
                }, 0, getPingInterval(), MILLISECONDS);

    }

    /**
     * 正常关闭
     */
    public void release() {
        // 释放 WebSocket
        if (mWebSocket != null) {
            mWebSocket.release();
        }

        // 释放线程池
        if (mExecutor != null) {
            mExecutor.shutdown();
            mExecutor = null;
        }
    }

    /**
     * 连接失败
     *
     * @param reconnect 是否要重新连接
     */
    @Override
    public void onConnectFailure(boolean reconnect) {

        if (reconnect) { // 需要重连

            JLogUtils.getDefault()
                    .title(TAG)
                    .enterContent()
                    .content("onConnectFailure: 进行重连")
                    .showInfo();

            reconnectBefore();

            connect();

        } else {  //不需要进行重连，则直接中断

            JLogUtils.getDefault()
                    .title(TAG)
                    .enterContent()
                    .content("onConnectFailure: 不进行重连")
                    .showWarn();

        }

    }

    @Override
    public void sendMsg(String msg) {

    }

    @Override
    public abstract void onMessage(String msg);

    @Override
    public abstract void onMessage(ByteString msg);

    /**
     * 获取时间间隔，单位毫秒{@link java.util.concurrent.TimeUnit#MILLISECONDS}
     */
    protected int getPingInterval() {
        return PING_INTERVAL;
    }

    /**
     * 进行重连前的操作
     */
    protected void reconnectBefore() {
    }

    /**
     * 在心跳包发送的时候顺便发送
     */
    protected void scheduleHandle(JWebSocketListener WebSocket) {
    }

    /**
     * 获取心跳包信息
     */
    protected abstract String getPingPongMsg();

    /**
     * 获取 WebSocket 的请求 url
     */
    protected abstract String getWebSocketUrl();

    /**
     * 获取 请求的 OkHttpClient 实例
     */
    protected abstract OkHttpClient getOkHttpClient();

}
