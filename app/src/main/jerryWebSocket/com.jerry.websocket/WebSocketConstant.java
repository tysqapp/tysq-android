package com.jerry.websocket;

/**
 * author       : frog
 * time         : 2019-09-11 18:00
 * desc         : WebSocket 的常量
 * version      : 1.4.0
 */
public interface WebSocketConstant {

    // ping（客户端实现）
    int PING = 1;
    // pong（服务端实现）
    int PONG = 2;
    // 发送token（客户端实现）
    int TOKEN = 3;
    // 推送未读数（服务端实现）
    int UNREAD = 4;
    // 设置通知已读（客户端实现）
    int READ = 5;
    // 退出（客户端实现）
    int QUIT = 6;

}
