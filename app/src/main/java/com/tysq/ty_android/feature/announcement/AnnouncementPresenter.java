package com.tysq.ty_android.feature.announcement;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.AdResp;

public final class AnnouncementPresenter extends BasePresenter<AnnouncementView> {
    @Inject
    public AnnouncementPresenter(AnnouncementView view) {
        super(view);
    }

    public void getAnnouncement(int start,
                                int pageSize,
                                boolean isFirst) {
        RetrofitFactory
                .getApiService()
                .getAdvertisement(start, pageSize, Constant.AnnouncementType.BANNER)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<AdResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onGetAnnouncementError(isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull AdResp value) {
                        mView.onGetAnnouncement(isFirst, value.getAdvertisementList());
                    }
                });
    }
}
