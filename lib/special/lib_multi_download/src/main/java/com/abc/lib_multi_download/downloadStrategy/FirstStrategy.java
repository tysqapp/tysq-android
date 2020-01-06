package com.abc.lib_multi_download.downloadStrategy;

import android.text.TextUtils;

import com.abc.lib_multi_download.config.DownloadConfig;
import com.abc.lib_multi_download.constants.RespHead;
import com.abc.lib_multi_download.exception.DownloadException;
import com.abc.lib_multi_download.model.DownloadInfo;
import com.abc.lib_multi_download.model.status.Status;
import com.abc.lib_multi_download.utils.DownloadFileUtils;
import com.abc.lib_multi_download.utils.DownloadUtils;
import com.abc.lib_multi_download.utils.NetUtils;
import com.abc.lib_utils.CloseUtils;
import com.abc.lib_utils.EncryptionUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.ResponseBody;

/**
 * author       : frog
 * time         : 2019-10-14 11:07
 * desc         : 首次传输策略
 * 进行步骤：
 * 1、进行请求
 * 2、计算文件夹名，确定唯一
 * 3、保存需要初始化的内容
 * <p>
 * 需要进行初始化的内容：
 * 1、fileName
 * 2、folderName
 * 3、totalSize
 * 4、rangeSize
 * 5、lastModify
 * 6、mimeType
 * 7、progress
 * version      : 1.0.0
 */
public class FirstStrategy extends BaseStrategy {

    private final static Object LOCK = new Object();
    // 临时后缀
    private String TMP = ".tmp";

    private final static String DOT = ".";

    public FirstStrategy(DownloadInfo downloadInfo) {
        super(downloadInfo);
    }

    @Override
    public boolean run() throws DownloadException {

        mResponse = NetUtils.request(mDownloadInfo.getUrl(), null);

        if (mResponse == null) {
            if (!isRunning) {
                return false;
            }
            throw new DownloadException("服务器异常", "Response is null!!");
        }

        if (!mResponse.isSuccessful()) {
            CloseUtils.close(mResponse);
            if (!isRunning) {
                return false;
            }
            throw new DownloadException("服务器异常", "[First Strategy]Response code is " + mResponse.code());
        }

        ResponseBody body = mResponse.body();

        if (body == null) {
            CloseUtils.close(mResponse);
            if (!isRunning) {
                return false;
            }
            throw new DownloadException("服务器异常", "Body is null.");
        }

        long contentLength = body.contentLength();
        if (contentLength <= 0) {
            CloseUtils.close(mResponse);
            if (!isRunning) {
                return false;
            }
            throw new DownloadException("服务器异常", "ContentLength is " + contentLength);
        }

        MediaType mediaType = body.contentType();

        if (mediaType == null) {
            CloseUtils.close(mResponse);
            if (!isRunning) {
                return false;
            }
            throw new DownloadException("服务器异常", "MediaType is null.");
        }

        String lastModified = mResponse.header(RespHead.LAST_MODIFIED);

        if (lastModified == null || lastModified.length() <= 0) {
            CloseUtils.close(mResponse);
            if (!isRunning) {
                return false;
            }
            throw new DownloadException("服务器异常", "lastModified is " + lastModified);
        }

        // 关闭响应
        CloseUtils.close(mResponse);

        // 初始化内容
        // 1、fileName
        // 2、folderName
        // 3、totalSize
        // 4、rangeSize
        // 5、lastModify
        // 6、mimeType
        // 7、progress
        boolean isCalculateSuc = calculateFileName();
        if (!isCalculateSuc) {
            if (!isRunning) {
                return false;
            }
            throw new DownloadException("文件创建失败", "fileName is null.");
        }

        mDownloadInfo.setTotalSize(contentLength);
        mDownloadInfo.setRangeSize(DownloadConfig.getInstance().getRangeSize());
        mDownloadInfo.setMimeType(mediaType.type() + "/" + mediaType.subtype());
        mDownloadInfo.setSubType(mediaType.subtype());
        mDownloadInfo.setLastModified(lastModified);
        mDownloadInfo.setProgress(0);
        mDownloadInfo.setDownloadSize(0);

        mDownloadInfo.setStatus(Status.DOWNLOAD);
        mDownloadInfo.update();

        return isRunning;
    }

    /**
     * 获取文件名称
     *
     * @return 成功则返回文件名称，否则返回null
     */
    private boolean calculateFileName() {

        String filename = mDownloadInfo.getFileName();

        if (TextUtils.isEmpty(filename)) {
            filename = obtainRandomName();
        }

        String fileNameWithoutSuffix = DownloadUtils.getFileNameWithoutSuffix(filename);
        String fileNameSuffix = DownloadUtils.getFileNameSuffix(filename);
        // 如果原来的文件名没后缀，则使用 mimeType 的值。
        if (fileNameSuffix.equals("")) {
            fileNameSuffix = mDownloadInfo.getSubType();
        }

        File result;
        int i = 1;

        String resultFileName = fileNameWithoutSuffix + DOT + fileNameSuffix;
        String resultFolderName = fileNameWithoutSuffix;

        while (true) {
            synchronized (LOCK) {

                boolean fileNameExist = DownloadFileUtils.isExist(resultFileName);
                boolean folderNameExist = DownloadFileUtils.isExist(resultFolderName);

                // 如果 成功的文件名 和 存储分片的文件夹 都不存在则可以使用该名称
                if (!fileNameExist && !folderNameExist) {
                    result = DownloadFileUtils.createFolder(resultFolderName);

                    mDownloadInfo.setFileName(resultFileName);
                    mDownloadInfo.setTmpFileName(resultFileName + TMP);
                    mDownloadInfo.setFolderName(resultFolderName);

                    break;
                }

                String tmp = fileNameWithoutSuffix + "(" + i + ")";
                resultFileName = tmp + DOT + fileNameSuffix;
                resultFolderName = tmp;

                ++i;
            }
        }

        if (result == null) {
            return false;
        }

        return true;

    }

    /**
     * 随机文件名字
     *
     * @return 文件名
     */
    private String obtainRandomName() {
        return EncryptionUtils.md5(mDownloadInfo.getUrl())
                + "-"
                + System.currentTimeMillis();
    }

}
