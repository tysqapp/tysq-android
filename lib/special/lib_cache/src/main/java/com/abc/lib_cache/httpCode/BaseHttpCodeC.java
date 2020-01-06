package com.abc.lib_cache.httpCode;

import android.text.TextUtils;

import com.abc.lib_cache.config.ProxyConstant;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * author       : frog
 * time         : 2019-09-20 17:52
 * email        : xxxxx
 * desc         : 报文解析
 * version      : 1.0.0
 */

public abstract class BaseHttpCodeC {

    /**
     * 读取一行数据
     *
     * @param is 输入流
     */
    public String readLine(InputStream is) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        boolean isMaybeEndOfLine = false;
        byte b;

        //一次读一个字节
        while ((b = (byte) is.read()) != -1) {

            bos.write(b);

            //如果当前读到一个 \r
            if (b == ProxyConstant.MESSAGE.CR) {
                isMaybeEndOfLine = true;
            } else if (isMaybeEndOfLine) {
                //读到 \n , 需要\r\n才能真正表明一行结束
                if (b == ProxyConstant.MESSAGE.LF) {
                    return bos.toString();
                }
                isMaybeEndOfLine = false;
            }
        }

        throw new IOException("Message Read Line Error." + bos.toString());
    }

    /**
     * 是不是空行
     *
     * @param line 行数据
     * @return true：空行；false：非空
     */
    protected boolean isEmptyLine(String line) {
        return TextUtils.equals(line, ProxyConstant.MESSAGE.CRLF);
    }

}
