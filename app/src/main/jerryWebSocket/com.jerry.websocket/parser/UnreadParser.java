package com.jerry.websocket.parser;

import com.jerry.websocket.WebSocketConstant;
import com.jerry.websocket.model.JWebSocketModel;
import com.jerry.websocket.model.WsUnreadModel;

/**
 * author       : frog
 * time         : 2019-09-11 18:03
 * desc         : 未读解析
 * version      : 1.4.0
 */
public class UnreadParser extends BaseParser<WsUnreadModel> {
    @Override
    protected int getCode() {
        return WebSocketConstant.UNREAD;
    }

    @Override
    protected JWebSocketModel parser(String data) {
        return parserToModel(data);
    }

    @Override
    protected Class getClazz() {
        return WsUnreadModel.class;
    }

}
