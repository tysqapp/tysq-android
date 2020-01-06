package com.jerry.websocket.parser;

import com.google.gson.Gson;
import com.jerry.websocket.model.JWebSocketModel;

import java.lang.reflect.Type;

/**
 * author       : frog
 * time         : 2019-09-11 17:48
 * desc         : 解析器基类
 * version      : 1.4.0
 */
public abstract class BaseParser<Model extends JWebSocketModel> {

    private Gson mGson = new Gson();
    //指向下一个处理类
    private BaseParser nextParser;

    public final JWebSocketModel parserMessage(int code, String data) {
        JWebSocketModel response = null;

        //判断是否是自己的处理等级
        if (code == getCode()) {
            // 是自己的处理等级，就将其进行处理
            response = this.parser(data);
        } else {

            // 如果还有下一个handler
            if (this.nextParser != null) {
                // 如果有就让下一个handler进行处理
                response = this.nextParser.parserMessage(code, data);
            }

        }

        return response;

    }

    /**
     * 设置下一个handler处理类
     *
     * @param nextParser
     */
    public void setNextParser(BaseParser nextParser) {
        this.nextParser = nextParser;
    }

    /**
     * 处理的code值
     *
     * @return
     */
    protected abstract int getCode();

    /**
     * 对任务的具体处理操作
     *
     * @param data
     */
    protected abstract JWebSocketModel parser(String data);

    protected abstract Class getClazz();

    protected Model parserToModel(String data) {
        return mGson.fromJson(data, (Type) getClazz());
    }

}
