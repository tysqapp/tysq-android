package com.tysq.ty_android.net.commonHeaderHandler;

import com.tysq.ty_android.net.commonHeaderHandler.handler.BaseHandler;
import com.tysq.ty_android.net.commonHeaderHandler.handler.TyCommonHeadHandler;

/**
 * author       : frog
 * time         : 2019/3/4 下午3:07
 * desc         :
 * version      : 1.3.0
 */
public class CommonParamsHandlerFactory {

    /**
     * 获取处理链
     *
     * @return 返回处理链
     */
    public static BaseHandler getHandler() {

        return new TyCommonHeadHandler();
    }

}
