package com.abc.lib_cache.urlStrategy.parser;

/**
 * author       : frog
 * time         : 2019-09-26 14:51
 * desc         : 解析工厂
 * version      : 1.0.0
 */
public class ParserFactory {

    /**
     * 获取 URL 解析链
     *
     * @return ParserHandler 解析器
     */
    public static ParserHandler getUrlParser() {
        ParserHandler m3u8 = new M3U8Handler();
        ParserHandler m3u8List = new M3U8ListHandler();
        ParserHandler redirect = new RedirectHandler();
        ParserHandler ts = new TsHandler();

        ts.setNextHandler(m3u8);
        m3u8.setNextHandler(m3u8List);
        m3u8List.setNextHandler(redirect);

        return ts;
    }

}
