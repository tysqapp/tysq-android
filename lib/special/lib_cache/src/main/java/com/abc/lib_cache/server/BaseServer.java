package com.abc.lib_cache.server;

import android.text.TextUtils;

import com.abc.lib_cache.socket.CacheProxySocket;
import com.abc.lib_cache.utils.SocketPoolInfo;
import com.abc.lib_log.JLogUtils;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * author       : frog
 * time         : 2019-09-20 17:35
 * email        : xxxxx
 * desc         : server的基类
 * version      : 1.0.0
 */

public abstract class BaseServer implements Runnable {

    // 端口重试次数 10次
    private static final int PORT_RETRY_TIMES = 10;

    // 是否正在循环监听
    private boolean mIsRunning;
    protected ServerSocket mServerSocket;

    // 连入socket 本地地址 [也就是服务器的ip地址加端口，例如：/127.0.0.1:16511]
    private String localInAddress;
    // 连入socket 远程地址
    private String remoteInAddress;

    /**
     * 是否正在运行
     *
     * @return
     */
    public boolean isRunning() {
        return mIsRunning                    //正在运行
                && mServerSocket != null     //serverSocket已经初始化[防null]
                && !mServerSocket.isClosed() //serverSocket未关闭
                && mServerSocket.isBound();  //serverSocket已经有绑定端口
    }

    public BaseServer() {
        JLogUtils
                .getDefault()
                .title(getClass().getCanonicalName())
                .content(Thread.currentThread().getName());
        this.mIsRunning = true;
    }

    @Override
    public void run() {

        JLogUtils logUtils = JLogUtils.getDefault();

        for (int curTimes = 0; curTimes < PORT_RETRY_TIMES; ++curTimes) {
            try {
                createServerSocket();
                setPortToConfig();

                logUtils.content("server type: " + getType() + " launch success!!!!")
                        .add("port").colon().add(mServerSocket.getLocalPort()).enterContent()
                        .add("retry times").colon().add((curTimes + 1)).enterContent()
                        .showInfo();

                while (mIsRunning) {
                    try {
                        Socket socketIn = mServerSocket.accept();
                        socketIn.setKeepAlive(true);

                        localInAddress = socketIn.getLocalSocketAddress().toString();
                        remoteInAddress = socketIn.getRemoteSocketAddress().toString();

                        logUtils.clear();

                        logUtils.add("server type").colon().add(getType()).enterContent()
                                .add("port").colon().add(mServerSocket.getLocalPort()).enterContent()
                                .add("local address").colon().add(localInAddress).enterContent()
                                .add("remote address").colon().add(remoteInAddress).enterContent();

                        //将 socket 放入socket池，remoteAddress->socket
                        synchronized (SocketPoolInfo.SOCKET_MAP) {
                            if (SocketPoolInfo.SOCKET_MAP.containsKey(remoteInAddress)) {
                                logUtils.add("The address(" + remoteInAddress + ") is exit!!!");
                                logUtils.showError();
                                return;
                            }

                            SocketPoolInfo.SOCKET_MAP.put(remoteInAddress, socketIn);
                        }

                        logUtils.showInfo();

                        SocketPoolInfo.executorPool.execute(getSocketIn(remoteInAddress));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (BindException e) {

                JLogUtils.getDefault()
                        .add("server type").colon().add(getType() + " launch fail!!!!").enterContent()
                        .add("error reason").colon().add(e.getMessage()).enterContent()
                        .add("retry port").enterContent()
                        .add("retry times").colon().add((curTimes + 1)).enterContent()
                        .showError();

                continue;

            } catch (IOException e) {

                JLogUtils.getDefault()
                        .add("server type").colon().add(getType() + " launch fail!!!!").enterContent()
                        .add("error reason").colon().add(e.getMessage()).enterContent()
                        .add("retry times").colon().add((curTimes + 1)).enterContent()
                        .showError();

            } catch (Exception e) {

                JLogUtils.getDefault()
                        .add(e.getMessage()).enterContent()
                        .showError();

            } finally {
                try {

                    if (mServerSocket != null) {
                        mServerSocket.close();
                    }

                    if (!TextUtils.isEmpty(remoteInAddress)) {
                        SocketPoolInfo.removeSocket(remoteInAddress);
                    }

                    JLogUtils.getDefault()
                            .add("server type").colon().add(getType() + " close success!!!!").enterContent()
                            .add("retry times").colon().add((curTimes + 1)).enterContent()
                            .showError();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            break;
        }

    }

    /**
     * 创建服务器侦听的ServerSocket
     */
    protected abstract void createServerSocket() throws IOException, BindException;

    /**
     * @return 标记当前类型，便于父类打印日志
     */
    protected abstract String getType();

    /**
     * 获取 SocketIn 的线程
     *
     * @param address 连入 socket 的地址
     * @return
     */
    protected abstract CacheProxySocket getSocketIn(String address);

    /**
     * 停止该服务
     */
    public void stop() {
        if (mServerSocket != null) {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mIsRunning = false;
    }

    /**
     * 设置正确的端口号
     */
    public abstract void setPortToConfig();

}
