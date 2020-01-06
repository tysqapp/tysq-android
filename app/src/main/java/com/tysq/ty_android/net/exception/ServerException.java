package com.tysq.ty_android.net.exception;

/**
 * author       : frog
 * time         : 2019/4/12 上午9:21
 * desc         : 服务器异常
 * version      : 1.3.0
 */
public class ServerException extends RuntimeException {
    private int code;
    private String message;

    public ServerException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
