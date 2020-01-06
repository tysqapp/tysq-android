package com.jerry.websocket;

import com.abc.lib_log.JLogUtils;
import com.abc.lib_websocket.manager.CacheWebSocketManager;
import com.jerry.websocket.model.JWebSocketModel;
import com.jerry.websocket.model.WsTokenModel;
import com.jerry.websocket.parser.ParserFactory;
import com.tysq.ty_android.net.OkHttpHelper;
import com.tysq.ty_android.net.cookie.PersistentCookieStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.OkHttpClient;
import okio.ByteString;

/**
 * author       : frog
 * time         : 2019-09-11 16:16
 * desc         : 我的 WebSocket 管理
 * version      : 1.0.0
 */
public class MyWebSocketManager extends CacheWebSocketManager {

    private static final String CODE = "code";
    private static final String DATA = "data";
    private static final int ERROR = -1;

    private final Map<Integer, List<JWebSocketDataListener>> mSubscribeListeners;
    private static final String WS = "ws://192.168.0.166:8081/api/notify/ws";

    MyWebSocketManager(Map<Integer, List<JWebSocketDataListener>> listeners) {
        super();
        this.mSubscribeListeners = listeners;
    }

    @Override
    public void onMessage(String msg) {
        JLogUtils jLogUtils = JLogUtils
                .getDefault()
                .title("MyWebSocketManager")
                .param("String")
                .param(WS)
                .enterContent();

        for (Map.Entry<Integer, List<JWebSocketDataListener>> entry : mSubscribeListeners.entrySet()) {
            jLogUtils.add(entry.getKey());
            jLogUtils.add(": ");
            jLogUtils.add(entry.getValue().toString());
            jLogUtils.enterContent();
        }

        jLogUtils.content(msg)
                .showInfo();

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        int code = ERROR;
        try {
            code = jsonObject.getInt(CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (code == ERROR) {
            return;
        }

        String data = null;
        try {
            data = jsonObject.getString(DATA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (data == null) {
            return;
        }

        JWebSocketModel webSocketModel = ParserFactory.PARSER.parserMessage(code, data);
        if (webSocketModel == null) {
            return;
        }

        for (Map.Entry<Integer, List<JWebSocketDataListener>> entry : mSubscribeListeners.entrySet()) {
            if (entry.getKey() == code) {
                for (JWebSocketDataListener listener : entry.getValue()) {
                    listener.onReceiveWebSocketData(webSocketModel);
                }
                return;
            }
        }
    }

    @Override
    protected void reconnectBefore() {

    }

    @Override
    public void onMessage(ByteString msg) {
        JLogUtils
                .getDefault()
                .title("MyWebSocketManager")
                .param("ByteString")
                .param(WS)
                .enterContent()
                .content(msg.toString())
                .showInfo();
    }

    @Override
    protected String getPingPongMsg() {
        return "{\"code\":1,\"data\":{}}";
    }

    @Override
    protected String getWebSocketUrl() {
        return WS;
    }

    @Override
    protected int getPingInterval() {
        return 3_000;
    }

    @Override
    protected OkHttpClient getOkHttpClient() {
        return OkHttpHelper.getOkHttpInstance();
    }

    private WsTokenModel getTokenModel(String token) {
        return new WsTokenModel(WebSocketConstant.TOKEN, token);
    }

    /**
     * 获取 cookie
     */
    private String getToken() {
        List<Cookie> cookies = PersistentCookieStore.getInstance().getCookies();

        if (cookies == null || cookies.size() <= 0) {
            return null;
        }

        Cookie cookie = cookies.get(0);
        if (cookie == null) {
            return null;
        }

        return cookie.value();
    }

}
