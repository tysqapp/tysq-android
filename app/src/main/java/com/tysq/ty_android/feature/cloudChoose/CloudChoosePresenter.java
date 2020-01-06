package com.tysq.ty_android.feature.cloudChoose;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import request.PersonInfoUpdateReq;
import request.VideoCoverReq;
import response.cloud.FileInfoResp;
import response.login.LoginResp;

public final class CloudChoosePresenter extends BasePresenter<CloudChooseView> {

    @Inject
    public CloudChoosePresenter(CloudChooseView view) {
        super(view);
    }

    public void load(final boolean isFirst,
                     int type,
                     int start,
                     int size,
                     String content) {
        RetrofitFactory
                .getApiService()
                .getCloudFileList(type, start, size, content)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<FileInfoResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onError(isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull FileInfoResp value) {
                        mView.onLoad(isFirst, value.getFileInfo());
                    }
                });
    }

    public void commitHeadChangeInfo(int id) {
        PersonInfoUpdateReq req = new PersonInfoUpdateReq(UserCache.getDefault().getAccount(),
                id,
                UserCache.getDefault().getHomeAddress(),
                UserCache.getDefault().getTrade(),
                UserCache.getDefault().getCareer(),
                UserCache.getDefault().getPersonalProfile());

        RetrofitFactory
                .getApiService()
                .putUpdatePersonData(req)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<LoginResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull LoginResp value) {
                        UserCache.save(value.getAccountInfo());
                        mView.onCommitHeadChangeInfo();
                    }
                });

    }

    public void commitCover(String url,
                            int coverId,
                            int fileId) {

        VideoCoverReq req = new VideoCoverReq(coverId, fileId);

        RetrofitFactory
                .getApiService()
                .postVideoCover(req)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onCommitCover(url, coverId, fileId);
                    }
                });

    }
}
