package thread;

import android.util.Log;

import com.tysq.ty_android.R;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.utils.TyFileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import common.RespData;
import listener.FileListener;
import listener.SliceListener;
import model.FileModel;
import model.SliceModel;
import okhttp.FileProgressRequestBody;
import okhttp3.MultipartBody;
import response.upload.FileUploadResp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.ErrorConstant;
import utils.UploadConfig;

/**
 * author       : frog
 * time         : 2019/6/13 下午3:08
 * desc         : 分片线程
 * version      : 1.3.0
 */
public class SliceThread extends Thread implements SliceListener {

    private static final String TAG = "SliceThread";
    // 类型
    private static final String MEDIA_TYPE = "application/octet-stream";
    // 错误重新尝试次数
    private static final int RETRY_TIMES = 3;
    // 充实
    private static final int RETRY_WAITING_DURATION = 50;
    // 没有任务
    private static final Integer NO_NEXT_TASK = -12;

    private final FileModel mFileModel;

    private final SliceModel mSliceModel;

    private volatile boolean isStop;

    private volatile long curSize;

    private FileListener mListener;

    private Response<RespData<FileUploadResp>> mResponse;

    SliceThread(String threadName,
                FileModel mFileModel,
                int sliceIndex,
                FileListener listener) {
        super(threadName);
        this.mFileModel = mFileModel;
        this.mSliceModel = new SliceModel();
        this.mSliceModel.setSliceName(mFileModel.getFilename() + "--" + sliceIndex);
        this.mSliceModel.setSliceIndex(sliceIndex);

        this.mListener = listener;

        this.isStop = false;
    }

    /**
     * 步骤：
     * 1、创建分片文件（三次机会创建）；
     * 2、从主文件拷贝到分片文件（加锁）（一次机会）；
     * 3、上传文件；
     * 4、删除分片文件；
     * 5、添加成功任务至成功列表；
     * 6、领取下一个任务（如果为空，则跳出循环）；
     */
    @Override
    public void run() {
        while (!isStop) {

            long startSize = calculateStartPos();

            mResponse = null;

            // 1、创建分片文件，此时文件为空，没内容
            File file = null;
            int retryTimes = 0;
            while (retryTimes < RETRY_TIMES) {
                file = createSliceFile(startSize);

                if (file != null) {
                    break;
                }

                if (isStop) {
                    break;
                }

                ++retryTimes;
                try {
                    sleep(RETRY_WAITING_DURATION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            // 文件创建失败，直接退出
            if (file == null) {
                error(ErrorConstant.FILE_CREATE_ERROR, "File create failure.");
                break;
            }

            // 2、拷贝至分片文件
            boolean copyResult = copyToSliceFile(file);

            if (!copyResult) {
                error(ErrorConstant.FILE_COPY_ERROR, "File copy failure.");
                break;
            }

            if (isStop) {
                break;
            }

            // 3、上传文件
            FileUploadResp fileUploadResp = null;
            try {
                fileUploadResp = uploadSliceFile(file);
            } catch (Exception e) {
                e.printStackTrace();
                error(ErrorConstant.FILE_REQUEST_ERROR, e.getMessage());
                break;
            }

            // 4、删除文件
            TyFileUtils.deleteFile(file);

            Log.i(TAG, getName() + " -- run: frog 1");

            if (isStop) {
                break;
            }

            Log.i(TAG, getName() + " -- run: frog 2");

            // 5、添加成功任务至成功列表
            putSucSliceToUploaded(fileUploadResp);

            Log.i(TAG, getName() + " -- run: frog 3");

            if (isStop) {
                break;
            }

            // 6、获取下一个任务
            Integer nextTask = getNextTask();

            Log.i(TAG, getName() + " -- run: frog 4");

            // 没有下一个任务，则退出
            if (nextTask.equals(NO_NEXT_TASK)) {
                Log.i(TAG, "[" + this.getName() + "] No task.");
                break;
            }

            mSliceModel.setSliceIndex(nextTask);
            mSliceModel.setSliceName(mFileModel.getFilename() + "--" + nextTask);

        }

        Log.i(TAG, "The thread[" + currentThread().getName() + "] exist.");
        if (mListener != null) {
            mListener.onExist(this, isStop);
        }

        onFinish();

    }

    /**
     * 回收
     */
    private void onFinish() {
        this.mListener = null;
        this.isStop = true;
        this.mResponse = null;
    }

    /**
     * 创建分片文件
     *
     * @param startSize 开始下标
     * @return 分片文件
     */
    private File createSliceFile(long startSize) {
        // 获取缓存文件路径
        File cacheDir = UploadConfig.CONTEXT.getCacheDir();

        try {
            return File.createTempFile(mFileModel.getFilename() + "-" + startSize,
                    ".tmp", cacheDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将分片 存至 文件
     *
     * @param sliceFile 保存的文件
     * @return true: 成功; false: 失败
     */
    private boolean copyToSliceFile(File sliceFile) {

        long startPos = calculateStartPos();
        long sliceLength = calculateSliceLength();

        File sourceFile = new File(mFileModel.getUrl());
        if (!sourceFile.exists()) {
            return false;
        }

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (inputStream == null) {
            return false;
        }

        // 跳至当前分片的字节
        long skip;
        try {
            skip = inputStream.skip(startPos);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (skip != startPos) {
            return false;
        }

        boolean isSaveSuc = saveToFile(sliceFile, inputStream, sliceLength);

        // 如果存出错，则返回错
        if (!isSaveSuc) {
            return false;
        }

        Log.i(TAG, "name: " + mSliceModel.getSliceName() + "\n" +
                "fileName: " + sliceFile.getName() + "\n" +
                "path: " + sliceFile.getAbsolutePath() + "\n" +
                "start: " + startPos + "\n" +
                "length: " + sliceLength + "\n" +
                "fileLength: " + sliceFile.length());

        return true;

    }

    private static boolean saveToFile(File file,
                                      InputStream inputStream,
                                      long length) {
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            byte[] tempBytes = new byte[2048];

            while (true) {
                // 读取的长度
                int readLength = inputStream.read(tempBytes);

                if (readLength == -1) {
                    fileOutputStream.flush();
                    // length 的长度 为0，则成功
                    return length <= 0;
                }

                int realLength;
                if (length < readLength) {
                    realLength = (int) length;
                } else {
                    realLength = readLength;
                }
                length -= readLength;

                fileOutputStream.write(tempBytes, 0, realLength);

                if (length <= 0) {
                    return true;
                }
            }

        } catch (IOException e) {

            e.printStackTrace();
            return false;

        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 计算开始的下标
     */
    private long calculateStartPos() {
        return (mSliceModel.getSliceIndex() - 1) * mFileModel.getSliceLength();
    }

    /**
     * 计算终止的下标
     */
    private long calculateSliceLength() {
        int sliceIndex = mSliceModel.getSliceIndex();
        int sliceCount = mFileModel.getSliceCount();

        long length;
        // 说明是最后一个分片，长度需要容纳最后剩下的全部
        if (sliceIndex == sliceCount) {
            length = mFileModel.getTotalSize()
                    - calculateStartPos();
        } else {
            length = mFileModel.getSliceLength();
        }
        return length;
    }

    /**
     * 将成功的文件存至已成功的列表中
     */
    private void putSucSliceToUploaded(FileUploadResp fileUploadResp) {
        synchronized (mFileModel.LOCK) {
            curSize = 0;

            ArrayList<Integer> uploaded = mFileModel.getUploaded();
            uploaded.add(mSliceModel.getSliceIndex());
        }
    }

    /**
     * 获取下一个任务
     */
    private Integer getNextTask() {
        synchronized (mFileModel.LOCK) {
            ArrayList<Integer> uploading = mFileModel.getUploading();
            if (uploading.size() <= 0) {
                return NO_NEXT_TASK;
            } else {
                return uploading.remove(0);
            }
        }
    }

    private void error(int code, String msg) {
        Log.e(TAG, "code: " + code + "; msg: " + msg);

        switch (code) {
            case ErrorConstant.FILE_CREATE_ERROR:
                setErrorMsg(code, R.string.upload_enough_storage);
                break;
            case ErrorConstant.FILE_COPY_ERROR:
                setErrorMsg(code, R.string.upload_copy_error);
                break;
            case ErrorConstant.FILE_REQUEST_ERROR:
                setErrorMsg(code, R.string.upload_net_error);
                break;
        }

    }

    private void setErrorMsg(int code, int errorId) {
        synchronized (mFileModel.LOCK) {
            mFileModel.setErrorMsg(code + ": " + UploadConfig.CONTEXT.getString(errorId));
            mFileModel.setError(true);
        }

        if (mListener != null) {
            mListener.onError(this);
        }

    }

    /**
     * 上传分片文件
     *
     * @param sliceFile 分片文件
     */
    private FileUploadResp uploadSliceFile(File sliceFile) throws Exception {

        Log.i(TAG, "文件提交：\n"
                + "chunk name: " + mSliceModel.getSliceName() + "\n"
                + "chunk number: " + mSliceModel.getSliceIndex() + "\n"
                + "total chunks " + mFileModel.getSliceCount() + "\n"
                + "chunks size: " + mFileModel.getSliceLength() + "\n"
                + "current chunk size: " + calculateSliceLength() + "\n"
                + "total size: " + mFileModel.getTotalSize() + "\n"
                + "hash: " + mFileModel.getHash() + "\n"
                + "filename: " + mFileModel.getFilename() + "\n"
                + "uploadFile: " + sliceFile.getAbsolutePath() + "\n"
                + "isStop: " + isStop + "\n"
        );

        FileProgressRequestBody filePart = new FileProgressRequestBody(
                sliceFile,
                MEDIA_TYPE,
                this);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("uploadfile", mFileModel.getFilename(), filePart);
        List<MultipartBody.Part> parts = builder.build().parts();

        Call<RespData<FileUploadResp>> call = RetrofitFactory
                .getApiService()
                .uploadFileSlice(
                        mSliceModel.getSliceIndex(),
                        mFileModel.getSliceCount(),
                        mFileModel.getSliceLength(),
                        calculateSliceLength(),
                        mFileModel.getTotalSize(),
                        mFileModel.getHash(),
                        mFileModel.getFilename(),
                        parts);

        call.enqueue(new Callback<RespData<FileUploadResp>>() {
            @Override
            public void onResponse(Call<RespData<FileUploadResp>> call,
                                   Response<RespData<FileUploadResp>> resp) {
                mResponse = resp;
                SliceThread.this.interrupt();
                Log.i(TAG, getName() + " -- run: frog 5");
            }

            @Override
            public void onFailure(Call<RespData<FileUploadResp>> call,
                                  Throwable t) {
                t.printStackTrace();
                SliceThread.this.interrupt();
                Log.i(TAG, getName() + " -- run: frog 6");
            }
        });

        // 等待请求完成
        try {
            join();
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }

        Log.i(TAG, getName() + " -- run: frog 7");

        // 如果需要中断，在线程中断后，先取消请求
        if (isStop) {
            if (!call.isCanceled()) {
                call.cancel();
            }
            // 中断返回null
            return null;
        }

        Log.i(TAG, getName() + " -- run: frog 8");

        if (mResponse == null) {
            throw new RuntimeException("Response is null.");
        }

        // 判断请求状态码
        if (!mResponse.isSuccessful()) {
            throw new RuntimeException("Failure to request.[" + sliceFile.getName() + "]" +
                    "; [code: " + mResponse.code() + "]");
        }

        RespData<FileUploadResp> body = mResponse.body();

        if (body == null) {
            throw new RuntimeException("Body is empty.");
        }

        if (body.getStatus() != 0) {
            throw new RuntimeException(body.getReason());
        }

        FileUploadResp data = body.getData();
        if (data == null) {
            throw new RuntimeException("Data is Empty.");
        }

        return data;

    }

    @Override
    public void onTransferred(long size) {
        curSize = size;
    }

    public long getProgress() {
        return curSize;
    }

    /**
     * 暂停线程
     */
    public synchronized void stopThread() {
        isStop = true;
    }

    /**
     * 线程是否停止
     *
     * @return true:已停止; false:未停止
     */
    public boolean isStop() {
        return isStop;
    }
}
