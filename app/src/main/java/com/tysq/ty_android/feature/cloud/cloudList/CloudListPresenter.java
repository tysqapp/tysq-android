package com.tysq.ty_android.feature.cloud.cloudList;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.local.sp.NetCache;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxObservableSubscriber;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;
import com.abc.lib_multi_download.jerry.JerryDownload;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import request.ChangeFilenameReq;
import request.CloudDeleteReq;
import response.cloud.FileListResp;

public final class CloudListPresenter extends BasePresenter<CloudListView> {
    @Inject
    public CloudListPresenter(CloudListView view) {
        super(view);
    }

    public void loadData(boolean isFirst, int start, int loadSize) {

        RetrofitFactory
                .getApiService()
                .getFileList(start, loadSize, 1)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<FileListResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadDataError(isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull FileListResp value) {
                        mView.onLoadData(isFirst, value.getTotalNumber(), value.getFileInfo());
                    }
                });

    }

    public void deleteFile(int fileId) {

        RetrofitFactory
                .getApiService()
                .deleteFile(new CloudDeleteReq(fileId))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onDeleteFile(fileId);
                    }
                });

    }

    public void putRename(int id, String filename) {

        RetrofitFactory
                .getApiService()
                .putChangeFilename(id, new ChangeFilenameReq(filename))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onPutRename(id, filename);
                    }
                });

    }

    public void download(final String filename,
                         final int accountId,
                         final String downloadUrl,
                         final String cover) {
        Observable
                .just(true)
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
//                        DownloadInfo download = JerryDownload.getInstance().download(
//                                downloadUrl,
//                                filename,
//                                accountId,
//                                NetCache.getDefault().getDomain(),
//                                cover
//                        );

                        JerryDownload.getInstance()
                                .download(downloadUrl,
                                        filename,
                                        accountId,
                                        NetCache.getDefault().getDomain(),
                                        cover
                                );

//                        if (download == null) {
//                            throw new Exception("save error");
//                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<Boolean>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onDownloadError();
                    }

                    @Override
                    protected void onSuccessRes(Boolean value) {
                        mView.onDownload();
                    }
                });
    }
}
