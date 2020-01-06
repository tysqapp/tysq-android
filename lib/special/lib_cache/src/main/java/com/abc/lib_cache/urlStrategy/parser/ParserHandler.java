package com.abc.lib_cache.urlStrategy.parser;

import com.abc.lib_cache.model.UrlInfo;

/**
 * author       : frog
 * time         : 2019-09-26 11:35
 * email        : xxxxx
 * desc         : 解析
 * version      : 1.0.0
 */

public abstract class ParserHandler {

    //指向下一个处理类
    private ParserHandler nextHandler;

    public final UrlInfo handleMessage(UrlInfo urlInfo) {

        //判断是否是自己的处理等级
        if (isHandler(urlInfo)) {
            //是自己的处理等级，就将其进行处理
            urlInfo.setType(getType());
            this.parser(urlInfo);
        } else {

            //如果还有下一个handler
            if (this.nextHandler != null) {
                //如果有就让下一个handler进行处理
                urlInfo = this.nextHandler.handleMessage(urlInfo);
            }

        }

        return urlInfo;

    }

    /**
     * 设置下一个handler处理类
     *
     * @param nextHandler 下一处理
     */
    void setNextHandler(ParserHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * 是否要处理
     *
     * @param urlInfo urlInfo
     * @return true 处理；false 通过
     */
    protected abstract boolean isHandler(UrlInfo urlInfo);

    /**
     * 对任务的具体处理操作
     *
     * @param urlInfo urlInfo
     */
    protected abstract void parser(UrlInfo urlInfo);

    /**
     * 获取类型
     */
    protected abstract String getType();

}