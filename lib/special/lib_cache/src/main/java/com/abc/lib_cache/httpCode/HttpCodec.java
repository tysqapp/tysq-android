package com.abc.lib_cache.httpCode;

import com.abc.lib_cache.config.ProxyConfig;
import com.abc.lib_cache.config.ProxyConstant;
import com.abc.lib_cache.message.RequestMessage;
import com.abc.lib_log.JLogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * author       : frog
 * time         : 2019-09-23 14:17
 * email        : xxxxx
 * desc         : 解析报文
 * version      : 1.0.0
 */

public class HttpCodec extends BaseHttpCodeC {

    private JLogUtils mLog;
    private int mBufferSize;

    public HttpCodec(JLogUtils log) {
        super();

        this.mBufferSize = ProxyConfig.getInstance().getBufferSize();
        this.mLog = log;
    }

    /**
     * 读取状态行【请求报文】
     *
     * @param is             输入流 （从客户端传来）
     * @param requestMessage 请求报文
     */
    public void readStateLine(InputStream is, RequestMessage requestMessage)
            throws IOException {

        // 读取一行数据（此时为 GET xxx?xx=xx HTTP/1.1）
        String stateLine = readLine(is);
        // 设置状态行
        requestMessage.setStateLine(stateLine);

        // 将 "GET xxx?xx=xx HTTP/1.1" 裁剪为
        // [0]=GET
        // [1]=xxx?xx=xx
        // [2]=HTTP/1.1
        String[] split = stateLine.split(" ");

        // 保存请求方法，"GET"
        String method;
        if (split.length < 1) {
            method = ProxyConstant.METHOD.NONE;
        } else {
            method = split[0];
        }
        requestMessage.setMethod(method);

        String url;
        if (split.length >= 2) {
            url = split[1];
        } else {
            url = null;
        }

        // 携带参数
        requestMessage.setUrl(url);

    }

    /**
     * 读取头部【请求报文】
     *
     * @param is             输入流
     * @param requestMessage 请求信息
     */
    public void readHeaders(InputStream is,
                            RequestMessage requestMessage) throws IOException {

        while (true) {
            // 例如：Host: 127.0.0.1
            String line = readLine(is);

            //如果读到空行 \r\n 响应头读完了
            if (isEmptyLine(line)) {
                break;
            }

            int index = line.indexOf(":");

            if (index > 0) {
                // 获取 key "Host"
                String key = line.substring(0, index);

                //如果 下标不对， 继续下次解析
                if (index + 2 >= line.length() - 2) {
                    continue;
                }

                // +2 是为了要跳过":"和" "（空格） -2是为了去掉\r\n
                // 获取 127.0.0.1
                String value = line.substring(index + 2, line.length() - 2);
                requestMessage.addHeader(key, value);
            }

        }

    }

    /**
     * 获取body【请求报文】
     *
     * @param is             输入流
     * @param requestMessage 响应信息
     */
    public void readBody(InputStream is, RequestMessage requestMessage) throws IOException {

        Map<String, List<String>> headers = requestMessage.getHeader();

        int contentLength = -1;
        // 读取 "Content-Length"
        if (headers.containsKey(ProxyConstant.RESP_HEAD.CONTENT_LENGTH)) {
            List<String> headerValue =
                    requestMessage.getHeaderValue(ProxyConstant.RESP_HEAD.CONTENT_LENGTH);
            contentLength = Integer.valueOf(headerValue.get(0));
        }

        if (contentLength > 0) {
            byte[] body = readBytes(is, contentLength);
            requestMessage.setBody(body);
        }

    }

    protected byte[] readBytes(InputStream is, int len) throws IOException {
        byte[] bytes = new byte[len];
        int readNum = 0;

        while (true) {
            int temp = is.read(bytes, readNum, len - readNum);
            if (temp == -1) {
                return bytes;
            }
            readNum += temp;

            //读取完毕
            if (readNum == len) {
                return bytes;
            }
        }
    }

    /**
     * 循环转输流
     *
     * @param totalLength 总共长度
     * @param os          输出流
     * @param is          输入流
     */
    private void loopTransportToStream(int totalLength,
                                       OutputStream os,
                                       InputStream is) throws IOException {
        while (totalLength > 0) {

            mLog.add("tran....(").add(totalLength).add(")").enterContent();

            int length = Math.min(totalLength, mBufferSize);

            byte[] bytes = readBytes(is, length);
            os.write(bytes);
            os.flush();
            totalLength -= length;

        }

        mLog.add("tran over!(").add(totalLength).add(")")
                .enterContent()
                .enterContent();
    }

}
