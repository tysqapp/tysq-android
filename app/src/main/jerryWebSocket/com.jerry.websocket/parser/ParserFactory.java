package com.jerry.websocket.parser;

/**
 * author       : frog
 * time         : 2019-09-11 18:13
 * desc         : 解析工厂
 * version      : 1.4.0
 */
public class ParserFactory {

    public static final BaseParser PARSER;
    static{
        PARSER = new UnreadParser();
    }

}
