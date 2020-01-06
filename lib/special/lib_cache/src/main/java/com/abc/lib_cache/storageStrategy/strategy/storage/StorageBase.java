package com.abc.lib_cache.storageStrategy.strategy.storage;

import com.abc.lib_cache.config.ProxyConstant;
import com.abc.lib_cache.lru.DiskLruCache;
import com.abc.lib_cache.lru.LruUtils;
import com.abc.lib_cache.message.RequestMessage;
import com.abc.lib_cache.storageStrategy.StrategyBase;
import com.abc.lib_cache.urlStrategy.IUrlInfo;
import com.abc.lib_cache.utils.CacheFileHelper;
import com.abc.lib_cache.utils.HttpHelper;
import com.abc.lib_log.JLogUtils;
import com.abc.lib_utils.CloseUtils;
import com.abc.lib_utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * author       : frog
 * time         : 2019-09-27 14:41
 * desc         : 缓存的抽象类
 * version      : 1.0.0
 */
public abstract class StorageBase extends StrategyBase {

    public StorageBase(IUrlInfo urlInfo,
                       RequestMessage requestMessage,
                       OutputStream outputStream,
                       JLogUtils log) {
        super(urlInfo, requestMessage, outputStream, log);
    }

    protected abstract DiskLruCache.Snapshot obtainFile();

    @Override
    public boolean run() {
        DiskLruCache.Snapshot snapshot = obtainFile();

        boolean isExistSnapShot = snapshot != null;

        log.enterContent();
        log.content("TsStorage");
        log.content("snapshot exists: " + isExistSnapShot);
        log.enterContent();

        // 如果文件存在，则直接返回
        if (isExistSnapShot) {
            transmitLocalStream(snapshot.getInputStream(LruUtils.INDEX_MSG),
                    snapshot.getInputStream(LruUtils.INDEX_BODY),
                    outputStream);
            snapshot.close();
            return true;
        }

        Response response = requestOriginal(requestMessage, urlInfo.getUrlInfo());

        if (response == null) {
            return false;
        }

        // 如果不成功，则将不成功的信息返回
        if (!response.isSuccessful()) {
            handleFailure(response,"response 请求失败！！直接回传");
            return false;
        }

        // 创建临时的文件，如果失败了则直接传回
        DiskLruCache.Editor editor = obtainLruEditor();
        if (editor == null) {
            handleFailure(response,"editor 创建失败！！直接回传");
            return false;
        }

        OutputStream msgFileOutputStream = null;
        OutputStream bodyFileOutputStream = null;
        try {
            msgFileOutputStream = editor.newOutputStream(LruUtils.INDEX_MSG);
            bodyFileOutputStream = editor.newOutputStream(LruUtils.INDEX_BODY);
        } catch (IOException e) {
            e.printStackTrace();

            handleFailure(response,"文件流 打开失败！！直接回传");

            CloseUtils.close(msgFileOutputStream);
            CloseUtils.close(bodyFileOutputStream);

            try {
                editor.abort();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return false;
        }

        // 转储结果
        boolean isSaveSuccess = transmitStream(response,
                outputStream,
                msgFileOutputStream,
                bodyFileOutputStream);

        response.close();

        log.content("本地存储结果：" + isSaveSuccess);

        // 转储不成功，则终止
        if (isSaveSuccess) {
            try {
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            try {
                editor.abort();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }


    }

    /**
     * 创建 LRU 的存储文件
     *
     * @return Editor
     */
    private DiskLruCache.Editor obtainLruEditor() {
        return LruUtils.getInstance().obtainEditor(getFileName());
    }

    /**
     * 传输本地文件
     *
     * @param msgInput 报文文件
     * @param resInput 视频文件
     */
    private void transmitLocalStream(InputStream msgInput,
                                     InputStream resInput,
                                     OutputStream outputStream) {
        InputStream msgBufInput = new BufferedInputStream(msgInput);
        InputStream resBufInput = new BufferedInputStream(resInput);

        CacheFileHelper.transmitStream(msgBufInput, outputStream, false);
        CacheFileHelper.transmitStream(resBufInput, outputStream, false);

        CloseUtils.close(msgInput);
        CloseUtils.close(resInput);
        CloseUtils.close(msgBufInput);
        CloseUtils.close(resBufInput);
    }

    /**
     * 将远程流转给本地
     *
     * @param response     返回的response
     * @param outputStream 输出流
     */
    private boolean transmitStream(Response response,
                                   OutputStream outputStream,
                                   OutputStream msgTempFileOutputStream,
                                   OutputStream tempFileOutputStream) {
        boolean result = false;

        String status = HttpHelper.getStatus(response);

        try {
            byte[] bytes = status.getBytes();
            outputStream.write(bytes);
            msgTempFileOutputStream.write(bytes);
            byte[] crlf = ProxyConstant.MESSAGE.CRLF.getBytes();
            outputStream.write(crlf);
            msgTempFileOutputStream.write(crlf);

            for (Map.Entry<String, List<String>> entry : response.headers().toMultimap().entrySet()) {

                for (String value : entry.getValue()) {
                    byte[] keyBytes = entry.getKey().getBytes();
                    outputStream.write(keyBytes);
                    msgTempFileOutputStream.write(keyBytes);

                    keyBytes = ":".getBytes();
                    outputStream.write(keyBytes);
                    msgTempFileOutputStream.write(keyBytes);

                    keyBytes = value.getBytes();
                    outputStream.write(keyBytes);
                    msgTempFileOutputStream.write(keyBytes);

                    keyBytes = ProxyConstant.MESSAGE.CRLF.getBytes();
                    outputStream.write(keyBytes);
                    msgTempFileOutputStream.write(keyBytes);
                }

            }

            byte[] crlfBytes = ProxyConstant.MESSAGE.CRLF.getBytes();
            outputStream.write(crlfBytes);
            msgTempFileOutputStream.write(crlfBytes);

            if (response.body() != null) {
                result = FileUtils.transmitStream(response.body().byteStream(),
                        outputStream,
                        tempFileOutputStream,
                        false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    /**
     * 处理不成功的情况
     *
     * @param response 响应报文
     */
    private void handleFailure(Response response,String failureMsg){
        transmitStream(response, outputStream);
        log.content(failureMsg);
        response.close();
    }

}
