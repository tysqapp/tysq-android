package com.abc.lib_cache.config;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : frog
 * time         : 2019-09-23 14:20
 * desc         : 代理的常量
 * version      : 1.0.0
 */
public class ProxyConstant {

    public interface SCHEMA_TYPE {
        String HTTP = "http";
        String HTTPS = "https";
    }

    /**
     * HTTP 方法类型
     */
    public interface METHOD {
        String CONNECT = "CONNECT";
        String NONE = "NONE";
    }

    /**
     * 代理链接的请求参数
     */
    public interface PARAM {
        // 原路径
        String ORIGINAL_URL = "original_url";
        // 码率
        String P = "p";
    }

    /**
     * 请求头部
     */
    public interface REQ_HEAD {
        // host
        String HOST = "Host";
    }

    /**
     * 响应头部
     */
    public interface RESP_HEAD {
        // 内容长度
        String CONTENT_LENGTH = "Content-Length";

        String LOCATION = "location";
    }

    public interface MESSAGE {
        // 回车和换行
        String CRLF = "\r\n";
        // \r
        int CR = 13;
        // \n
        int LF = 10;
    }

    public static final List<String> CODE_RATE_LIST = new ArrayList();

    public static final String DEFAULT_CODE_RATE;

    static {
        CODE_RATE_LIST.add("240");
        CODE_RATE_LIST.add("480");
        CODE_RATE_LIST.add("720");
        CODE_RATE_LIST.add("1080");

        DEFAULT_CODE_RATE = "240";
    }

    /**
     * 请求样式
     */
    public interface ResType {
        // ts 文件
        String TS = "TS";
        // m3u8 的索引文件
        String M3U8 = "M3U8";
        // m3u8 索引的索引
        String M3U8_LIST = "M3U8_LIST";
        // 重定向
        String REDIRECT = "REDIRECT";
        // 处理出错
        String ERROR = "ERROR";
    }
}
