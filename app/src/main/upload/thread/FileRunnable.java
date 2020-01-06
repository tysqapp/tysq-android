package thread;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.tysq.ty_android.R;
import com.tysq.ty_android.net.RetrofitFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import common.RespData;
import factory.UploadFactory;
import listener.FileListener;
import model.FileModel;
import model.SliceModel;
import model.UploadUpdateEvent;
import request.MergeFileReq;
import response.cloud.FileInfoResp;
import response.upload.FileInfoCheckResp;
import response.upload.FileMergeResp;
import retrofit2.Call;
import retrofit2.Response;
import utils.ErrorConstant;
import utils.UploadUtils;

/**
 * author       : frog
 * time         : 2019/6/13 上午9:52
 * desc         : 文件线程
 * version      : 1.3.0
 * <p>
 * 该线程需要做的事情
 * 1、请求并验证hash是否正确
 */
public class FileRunnable implements Runnable, FileListener {

    private final static String TAG = "FileRunnable";

    // 进度更新间隔 1秒
    private final static int PROCESS_UPDATE_TIME_OFFSET = 1000;

    // 线程数
    private final static int MAX_THREAD_SIZE = 3;

    private final FileModel mFileModel;

    private final List<SliceThread> mThreadList = new ArrayList<>();

    private boolean isLoopToUpdateProgress = true;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public FileRunnable(FileModel fileModel) {
        this.mFileModel = fileModel;
    }

    @Override
    public void run() {

        // 获取文件
        File file = new File(mFileModel.getUrl());

        // 检测文件是否存在
        if (!file.exists()) {
            error(ErrorConstant.NOT_FIND_FILE,
                    "The file isn't exist.[" + file.getAbsolutePath() + "]");
            setError(R.string.upload_not_find_file);
            return;
        }

        // 进行 hash 计算
        long hashStart = System.currentTimeMillis();
        String hash = fileToSHA256(file);
        long hashEnd = System.currentTimeMillis();

        long takeTime = hashEnd - hashStart;
        if (TextUtils.isEmpty(hash)) {
            Log.e(TAG, "FileName: " + file.getName() + "\n" +
                    "hash:" + hash + "\n" +
                    "takeTime:" + takeTime);

            error(ErrorConstant.HASH_FAILURE,
                    "Hash failure.[" + file.getAbsolutePath() + "]");
            setError(R.string.upload_file_info_error);

            return;
        }

        Log.i(TAG, "FileName: " + file.getName() + "\n" +
                "hash:" + hash + "\n" +
                "takeTime:" + takeTime);

        // 将 hash 放入
        mFileModel.setHash(hash);

        FileInfoCheckResp fileInfoCheckResp;
        try {
            fileInfoCheckResp = checkFileInfo();
        } catch (Exception e) {
            e.printStackTrace();

            error(ErrorConstant.FILE_INFO_CHECK_FAILURE, e.getMessage());
            setError(e.getMessage());

            return;
        }

        // 已经上传成功
        if (fileInfoCheckResp.getStatus() == 1) {

            FileInfoResp.FileItem fileInfo = fileInfoCheckResp.getFileInfo();
            Log.i(TAG, "fileInfo suc: " + fileInfo.toString());

            mFileModel.setPercent(100);

            mHandler.post(() -> {
                if (mFileModel.getListener() != null) {
                    mFileModel.getListener().onProgress();
                }
            });

            // 线程等待1秒，让用户看得到效果
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            setSuccess(fileInfo);

            return;
        }

        List<String> fileSlice = fileInfoCheckResp.getFileSlice();

        ArrayList<Integer> sucSliceList = new ArrayList<>();
        try {
            for (String item : fileSlice) {
                Integer sucSliceIndex = Integer.parseInt(item);
                sucSliceList.add(sucSliceIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();

            error(ErrorConstant.PARSER_SUC_INDEX_FAILURE, "Parse the index error.");
            setError(R.string.upload_data_error);

            return;
        }

        // 将已经上传成功的文件放入列表
        mFileModel.getUploaded().clear();
        mFileModel.getUploaded().addAll(sucSliceList);

        // 设置分片大小
        mFileModel.setSliceLength(fileInfoCheckResp.getFileChunkSize());

        // 分片数
        int sliceCount = (int) (mFileModel.getTotalSize() / mFileModel.getSliceLength());
        if (mFileModel.getTotalSize() < mFileModel.getSliceLength()) {
            sliceCount = 1;
        }

        // 计算分片总数
        mFileModel.setSliceCount(sliceCount);

        // 添加需要上传的分片
        mFileModel.getUploading().clear();
        for (Integer i = 1; i <= mFileModel.getSliceCount(); ++i) {
            if (!sucSliceList.contains(i)) {
                mFileModel.getUploading().add(i);
            }
        }

        // 开线程，为了线程安全，需要加速
        int threadSize = Math.min(mFileModel.getUploading().size(), MAX_THREAD_SIZE);
//        synchronized (mFileModel.LOCK) {
        for (int i = 0; i < threadSize; ++i) {
            ArrayList<Integer> uploading = mFileModel.getUploading();

            // 处理的分片下标
            Integer curHandleSliceIndex = uploading.remove(0);

            SliceThread sliceThread = new SliceThread(
                    mFileModel.getFilename() + "--" + i,
                    mFileModel,
                    curHandleSliceIndex,
                    this);
            mThreadList.add(sliceThread);
        }
//        }

        for (SliceThread sliceThread : mThreadList) {
            sliceThread.start();
        }

        if (mThreadList.size() > 0) {
            while (isLoopToUpdateProgress) {
                try {
                    Thread.sleep(PROCESS_UPDATE_TIME_OFFSET);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 如果没有监听器，就不必做多余的计算
                if (mFileModel.getListener() == null) {
                    continue;
                }

                calculateProgress();

            }
        } else {
            calculateProgress();
        }

        Log.i(TAG, "Start to merge.");

        // 进行合并请求
        FileMergeResp fileMergeResp = null;
        try {
            fileMergeResp = mergeFileInfo();
        } catch (Exception e) {
            e.printStackTrace();

            error(ErrorConstant.FILE_MERGE_FAILURE, e.getMessage());
            setError(e.getMessage());
            return;
        }

        // 设置进度
        mFileModel.setPercent(100);

        mHandler.post(() -> {
            if (mFileModel.getListener() != null) {
                mFileModel.getListener().onProgress();
            }
        });

        // 线程等待1秒，让用户看得到效果
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setSuccess(fileMergeResp.getFileInfo());

//        calculateProgress();

    }

    private void calculateProgress() {
        synchronized (mFileModel.LOCK) {
            int uploadedSize = mFileModel.getUploaded().size();
            long sliceLength = mFileModel.getSliceLength();

            // 如果含有最后一片，需要加上多出的
            long overflowLength = 0;
            if (mFileModel.getUploaded().contains(mFileModel.getSliceCount())) {
                overflowLength = mFileModel.getTotalSize() % mFileModel.getSliceLength();
            }

            long realLength = sliceLength * uploadedSize + overflowLength;

            StringBuilder stringBuilder = new StringBuilder();

            for (SliceThread sliceThread : mThreadList) {
                long progress = sliceThread.getProgress();

                stringBuilder.append(sliceThread.getName());
                stringBuilder.append(": ");
                stringBuilder.append(progress);
                stringBuilder.append("\n");

                realLength += progress;
            }

            int percent = (int) ((realLength * 100) / mFileModel.getTotalSize());

            if (percent > 98) {
                percent = 98;
            }

            // 设置进度
            mFileModel.setPercent(percent);

            mHandler.post(() -> {
                if (mFileModel.getListener() != null) {
                    mFileModel.getListener().onProgress();
                }
            });

            Log.i(TAG, "[" + mFileModel.getFilename() + "]\n" +
                    "uploadedSize: " + uploadedSize + "\n" +
                    "sliceLength: " + sliceLength + "\n" +
                    "overflowLength: " + overflowLength + "\n" +
                    "realLength: " + realLength + "\n" +
                    "[\n" +
                    stringBuilder.toString() +
                    "]\n" +
                    "progress: " + percent + "%");

        }
    }

    /**
     * 检测文件是否存在网络请求
     */
    private FileMergeResp mergeFileInfo() throws IOException {

        MergeFileReq mergeFileReq = new MergeFileReq(mFileModel.getSliceCount(), mFileModel.getHash());
        Call<RespData<FileMergeResp>> call = RetrofitFactory.getApiService()
                .mergeFileInfo(mergeFileReq);

        Response<RespData<FileMergeResp>> execute = call.execute();

        // 请求不成功
        if (!execute.isSuccessful()) {
            throw new RuntimeException(
                    execute.errorBody() == null ?
                            "Request is failure." : execute.message());
        }

        RespData<FileMergeResp> body = execute.body();

        if (body == null) {
            throw new RuntimeException("Body is null.");
        }

        if (body.getStatus() != 0) {
            throw new RuntimeException(body.getReason());
        }

        return body.getData();

    }

    /**
     * 检测文件是否存在网络请求
     */
    private FileInfoCheckResp checkFileInfo() throws IOException {

        Call<RespData<FileInfoCheckResp>> call = RetrofitFactory.getApiService()
                .getFileInfo(mFileModel.getHash(), mFileModel.getFilename());

        Response<RespData<FileInfoCheckResp>> execute = call.execute();

        // 请求不成功
        if (!execute.isSuccessful()) {
            throw new RuntimeException(
                    execute.errorBody() == null ?
                            "Request is failure." : execute.message());
        }

        RespData<FileInfoCheckResp> body = execute.body();

        if (body == null) {
            throw new RuntimeException("Body is null.");
        }

        if (body.getStatus() != 0) {
            throw new RuntimeException(body.getReason());
        }

        return body.getData();

    }

    private String fileToSHA256(File file) {
        InputStream inputStream = null;
        try {
            // Create an FileInputStream instance according to the filepath
            inputStream = new FileInputStream(file);
            // The buffer to read the file
            byte[] buffer = new byte[1024];
            // Get a SHA-256 instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Record how many bytes have been read
            int numRead = 0;
            while (numRead != -1) {
                numRead = inputStream.read(buffer);
                if (numRead > 0) {
                    // Update the digest
                    digest.update(buffer, 0, numRead);
                }
            }
            // Complete the hash computing
            byte[] sha1Bytes = digest.digest();
            // Call the function to convert to hex digits
            return convertHashToString(sha1Bytes);

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (inputStream != null) {
                try {
                    // Close the InputStream
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * Convert the hash bytes to hex digits string
     *
     * @return The converted hex digits string
     */
    private String convertHashToString(byte[] hashBytes) {
        StringBuilder returnVal = new StringBuilder();
        for (byte hashByte : hashBytes) {
            returnVal.append(
                    Integer.toString((hashByte & 0xff) + 0x100, 16)
                            .substring(1)
            );
        }
        return returnVal.toString().toLowerCase();
    }

    /**
     * 错误信息
     *
     * @param code 状态码
     * @param msg  提示信息
     */
    private void error(int code, String msg) {
        Log.e(TAG, "code: " + code + "; msg: " + msg);
    }

    @Override
    public void onExist(SliceThread thread, boolean isStop) {
        synchronized (mFileModel.LOCK) {
            mThreadList.remove(thread);
            if (mThreadList.size() <= 0) {
                isLoopToUpdateProgress = false;
            }

            // 暂停所有的线程
            if (isStop) {
                stop();
            }
        }
    }

    @Override
    public void onFinishTask(SliceThread thread, SliceModel sliceModel) {

    }

    @Override
    public synchronized void onError(SliceThread thread) {
        Log.e(TAG, "[" + thread.getName() + "] 线程异常.");

        // 一条线程有问题，停止其他的线程
        for (SliceThread sliceThread : mThreadList) {
            if (sliceThread != thread) {
                sliceThread.stopThread();
            }
        }

        updateError();
    }

    /**
     * 暂停所有线程
     */
    public void stop() {
        for (SliceThread sliceThread : mThreadList) {
            if (!sliceThread.isStop()) {
                sliceThread.stopThread();
            }
        }
    }

    /**
     * 设置错误
     *
     * @param errorStringId 错误string id
     */
    private void setError(int errorStringId) {
        setError(UploadUtils.getString(errorStringId));
    }

    /**
     * 设置错误
     *
     * @param errorString 错误信息 string
     */
    private void setError(String errorString) {
        mFileModel.setErrorMsg(errorString);
        mFileModel.setError(true);

        updateError();
    }

    /**
     * 设置成功
     *
     * @param fileItem 成功的文件
     */
    private void setSuccess(@NonNull FileInfoResp.FileItem fileItem) {
        // 传递给已上传
        EventBus.getDefault().post(new UploadUpdateEvent(fileItem, mFileModel));

        UploadFactory.removeModel(mFileModel);
    }

    private void updateError() {
        // 刷新错误信息
        mHandler.post(() -> {
            if (mFileModel.getListener() != null) {
                mFileModel.getListener().onError();
                mFileModel.setListener(null);
            }
        });
    }

}
