package com.abc.lib_cache.message;

import com.abc.lib_log.JLogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author       : frog
 * time         : 2019-09-20 17:56
 * email        : xxxxx
 * desc         : 请求报文
 * version      : 1.0.0
 */

public class RequestMessage {

    // 状态行
    private String stateLine;
    // 头部
    private Map<String, List<String>> header = new HashMap<>();

    // 方法
    private String method;

    // 请求的url
    private String url;

    // body
    private byte[] body;

    public RequestMessage() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void addHeader(String key, String value) {
        if (header.containsKey(key)) {
            List<String> valueList = header.get(key);
            valueList.add(value);
        } else {
            List<String> valueList = new ArrayList<>();
            valueList.add(value);
            header.put(key, valueList);
        }
    }

    public List<String> getHeaderValue(String key) {
        return header.get(key);
    }

    public String getStateLine() {
        return stateLine;
    }

    public void setStateLine(String stateLine) {
        this.stateLine = stateLine;
    }

    public Map<String, List<String>> getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void showLog() {
        JLogUtils log = JLogUtils.getDefault().content(stateLine);

        for (Map.Entry<String, List<String>> entry : header.entrySet()) {

            for (String value : entry.getValue()) {
                log.add(entry.getKey());
                log.colon();
                log.add(" ");
                log.add(value);
                log.enterParam();
            }

        }

        log.showInfo();
    }

    /**
     * 打印日志
     *
     * @param log 日志
     */
    public void showLog(JLogUtils log) {

        log.title("request message")
                .add(stateLine.substring(0, stateLine.length() - 2)).enterContent();

        // 打印头部
        for (Map.Entry<String, List<String>> entry : header.entrySet()) {

            for (String value : entry.getValue()) {
                log.add(entry.getKey())
                        .colon()
                        .add(value)
                        .enterContent();
            }

        }

        //添加body
        if (body == null) {
            log.content("Body is empty.");
        } else {
            log.add("body length").colon().add(body.length).enterContent();
        }

        log.enterContent();

    }

    /**
     * 清理
     */
    public void clear() {
        this.method = null;
        this.body = null;
        this.stateLine = null;
        this.url = null;
        this.header.clear();
    }

}
