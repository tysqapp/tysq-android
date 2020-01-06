package com.tysq.ty_android.utils.download;

import android.graphics.Bitmap;
import android.util.Log;

import com.abc.lib_utils.toast.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadHelper {
    public static String TAG = DownloadHelper.class.getSimpleName();

    public static Observable<Progress> download(final String url,
                                                final File file,
                                                final boolean returnProgress) {
        return Observable.create(new ObservableOnSubscribe<Progress>() {
            @Override
            public void subscribe(ObservableEmitter<Progress> emitter) throws Exception {

                OkHttpClient httpClient = addProgressResponseListener(new OkHttpClient(), new ProgressResponseBody.ProgressListener() {
                    @Override
                    public void onProgress(long currentBytes, long contentLength, boolean done) {
                        if (returnProgress) {
                            emitter.onNext(new Progress(currentBytes, contentLength, done));
                        }
                    }
                });

                try {
                    download(httpClient, url, file);

                    emitter.onNext(new Progress());

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    emitter.onError(e);
                }

                emitter.onComplete();
            }
        });
    }

    public static Observable<Progress> downloadBase64(final String url, final File file) {
        return Observable.create(new ObservableOnSubscribe<Progress>() {

            @Override
            public void subscribe(ObservableEmitter<Progress> emitter) throws Exception {
                try {

                    boolean isPNG = url.contains("png");

                    String temp = url.substring(url.indexOf(";base64,"));
                    temp = temp.replaceFirst(";base64,", "");

                    Bitmap bitmap = BitmapUtil.getBytesFromBase64(temp);

                    if (isPNG) {
                        BitmapUtil.saveBitmap2PNG(bitmap, 100, file);
                    } else {
                        BitmapUtil.saveBitmap2JPG(bitmap, 100, file);
                    }

                    emitter.onNext(new Progress());

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    emitter.onError(e);
                }

                emitter.onComplete();
            }
        });
    }

    public static Observable<Progress> downloadVerifyMD5(final String url, final File file, final String md5, final boolean returnProgress) {
        return Observable.create(new ObservableOnSubscribe<Progress>() {

            @Override
            public void subscribe(ObservableEmitter<Progress> emitter) throws Exception {
                OkHttpClient httpClient = addProgressResponseListener(new OkHttpClient(), new ProgressResponseBody.ProgressListener() {
                    @Override
                    public void onProgress(long currentBytes, long contentLength, boolean done) {
                        if (returnProgress) {
                            emitter.onNext(new Progress(currentBytes, contentLength, done));
                        }
                    }
                });
                try {
                    download(httpClient, url, file);
                    boolean verify = DigestUtils.checkFileMD5(file, md5);
                    if (verify) {
                        emitter.onComplete();
                    } else {
                        file.delete();
                        emitter.onError(new Throwable("校验错误"));
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    emitter.onError(e);
                }
            }
        });
    }

    public static OkHttpClient addProgressResponseListener(OkHttpClient client,
                                                           final ProgressResponseBody.ProgressListener progressListener) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //拦截
                Response originalResponse = chain.proceed(chain.request());
                //包装响应体并返回
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        };
        return client.newBuilder()
                .addInterceptor(interceptor)
                .build();
    }

    public static void write(File file, byte[] contentInBytes) {
        FileOutputStream fop = null;
        try {
            if (file.exists()) {
                file.createNewFile();
            } else {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }
            fop = new FileOutputStream(file);
            fop.write(contentInBytes);
            fop.flush();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            IOUtil.closeIO(fop);
        }
    }

    private static void download(OkHttpClient httpClient, String url, final File file) throws Exception {
        Log.d("文件下载", url);
        if (url == null || url.trim().length() == 0 || url.startsWith("null")) {
            Log.e(TAG, "错误链接：" + url);
            ToastUtils.show("链接异常，请检查后重试");
        }
        try {
            Request request = new Request.Builder().get().url(url).build();
            Response response = httpClient.newCall(request).execute();
            byte[] result = response.body().bytes();
            if (response.code() >= 400) {
                Log.e(TAG, response.code() + "");
                ToastUtils.show("网络异常，请检查后重试");
            }
            write(file, result);
            Log.d("下载成功", file.getAbsoluteFile().getAbsolutePath());
        } catch (Exception e) {
            ToastUtils.show("下载失败，请重新尝试");
        }
    }

    public static class Progress {
        public long currentBytes;
        public long contentLength;
        public boolean done;

        public Progress() {
        }

        public Progress(long currentBytes, long contentLength, boolean done) {
            this.currentBytes = currentBytes;
            this.contentLength = contentLength;
            this.done = done;
        }
    }

}
