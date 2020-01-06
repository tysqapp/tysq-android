package com.abc.lib_utils;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

/**
 * author       : frog
 * time         : 2019-09-24 11:06
 * email        : xxxxx
 * desc         : 流工具
 * version      : 1.0.0
 */

public class CloseUtils {

    /**
     * 关闭流
     *
     * @param closeable 需要关闭的流
     */
    public static void close(Closeable closeable) {

        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭 socket
     *
     * @param socket 流
     */
    public static void close(Socket socket) {

        if (socket == null) {
            return;
        }

        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
