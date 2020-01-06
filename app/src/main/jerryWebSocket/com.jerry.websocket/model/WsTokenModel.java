package com.jerry.websocket.model;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-09-12 16:45
 * desc         : WebSocket 发送 token
 * version      : 1.4.0
 */
public class WsTokenModel {


    /**
     * code : 3
     * data : {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NjgxODcwNjksImlhdCI6MTU2ODEwMDY2OSwiaWQiOjMzOSwibmJmIjoxNTY4MTAwNjY5fQ.BZ_ikoP8GG1MHghcWISoueYZDWo9flXZaYiXjNRRcr8"}
     */

    @SerializedName("code")
    private int code;
    @SerializedName("data")
    private DataBean data;

    public WsTokenModel(int code, String token) {
        this.code = code;
        this.data = new DataBean(token);
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
         * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NjgxODcwNjksImlhdCI6MTU2ODEwMDY2OSwiaWQiOjMzOSwibmJmIjoxNTY4MTAwNjY5fQ.BZ_ikoP8GG1MHghcWISoueYZDWo9flXZaYiXjNRRcr8
         */

        @SerializedName("token")
        private String token;

        public DataBean(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
