package com.abc.lib_cache.utils;

import com.abc.lib_utils.CloseUtils;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author       : frog
 * time         : 2019-09-20 17:48
 * email        : xxxxx
 * desc         : 所有Socket池的信息
 * 1、本地代理 的 两个Socket池：WebView连LocalProxy的SocketIn 和 LocalProxy连MITMServer的SocketOut
 * 2、MITM 的 两个Socket池：LocalProxy连MITMServer的SocketIn 和 MITMServer连MITMProxy的SocketOut
 * 3、MITM代理 的 两个Socket池：MITMServer连MITMProxy的SocketIn 和 MITMProxy连源服务器或远程代理服务器的SocketOut
 * <p>
 * version      : 1.0.0
 */

public class SocketPoolInfo {

    public static final Map<String, Socket> SOCKET_MAP= new HashMap<>();

    public static final ExecutorService executorPool = Executors.newCachedThreadPool();

    public static void removeSocket(String address) {
        synchronized (SocketPoolInfo.SOCKET_MAP) {
            Socket socket = SocketPoolInfo.SOCKET_MAP.remove(address);
            CloseUtils.close(socket);
        }
    }

    public static Socket getSocket(String address){
        synchronized (SocketPoolInfo.SOCKET_MAP) {
            return SocketPoolInfo.SOCKET_MAP.get(address);
        }
    }

}