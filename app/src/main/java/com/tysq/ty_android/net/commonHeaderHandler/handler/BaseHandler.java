package com.tysq.ty_android.net.commonHeaderHandler.handler;

import java.util.Map;

import okhttp3.Request;

/**
 * author       : frog
 * time         : 2019/3/4 下午2:28
 * desc         : 职责链处理者基类
 * version      : 1.3.0
 */
public abstract class BaseHandler {

    /**
     * 下一个处理者
     */
    private BaseHandler mNextHandler;

    /**
     * 获取对应的签名头参数
     */
    public final void getHeaderParams(Request request, Map<String, String> headParams) {

        if (isHandle(request, headParams)) {
            handle(request, headParams);
        }

        if (mNextHandler != null) {
            mNextHandler.getHeaderParams(request, headParams);
        }

    }

    /**
     * 设置下一个处理者
     *
     * @param next 下一处理者
     */
    public void setNextHandler(BaseHandler next) {
        this.mNextHandler = next;
    }

    /**
     * 获取当前处理者是否要进行处理
     *
     * @return true：需要处理；false：不需要处理
     */
    protected abstract boolean isHandle(Request request, Map<String, String> headParams);

    /**
     * 当前处理者处理的逻辑
     *
     * @param request    请求
     * @param headParams 头部参数
     */
    protected abstract void handle(Request request, Map<String, String> headParams);

}
