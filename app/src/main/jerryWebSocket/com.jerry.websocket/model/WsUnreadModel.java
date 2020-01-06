package com.jerry.websocket.model;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-09-11 18:05
 * desc         : 未读数量
 * version      : 1.4.0
 */
public class WsUnreadModel implements JWebSocketModel {

    /**
     * {unread : 4}
     */

    @SerializedName("unread")
    private int unread;

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
}
