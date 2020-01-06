package com.jerry.websocket.model;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-09-12 16:53
 * desc         : 通知已读
 * version      : 1.4.0
 */
public class WsRemindModel {

    /**
     * code : 5
     * data : {"remind_id":123}
     */

    @SerializedName("code")
    private int code;
    @SerializedName("data")
    private DataBean data;

    public WsRemindModel(int code, String remindId) {
        this.code = code;
        this.data = new DataBean(remindId);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * remind_id : 123
         */

        @SerializedName("remind_id")
        private String remindId;

        public DataBean(String remindId) {
            this.remindId = remindId;
        }

        public String getRemindId() {
            return remindId;
        }

        public void setRemindId(String remindId) {
            this.remindId = remindId;
        }
    }
}
