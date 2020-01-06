package com.tysq.ty_android.net.commonHeaderHandler.handler;

import java.util.Map;

import okhttp3.Request;

/**
 * author       : frog
 * time         : 2019/4/12 上午9:38
 * desc         : 增加通用参数
 * version      : 1.3.0
 */
public class TyCommonHeadHandler extends BaseHandler {

    @Override
    protected boolean isHandle(Request request, Map<String, String> headParams) {
        return true;
    }

    @Override
    protected void handle(Request request, Map<String, String> headerParams) {
//        headerParams.put(NetConfig.VERSION_NAME, BuildConfig.VERSION_NAME);



    }
}