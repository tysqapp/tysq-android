package com.tysq.ty_android.feature.reportDetail;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.report.ReportDetailResp;

public final class ReportDetailPresenter extends BasePresenter<ReportDetailView> {
    @Inject
    public ReportDetailPresenter(ReportDetailView view) {
      super(view);
    }

    public void getReportDetail(String id){
        RetrofitFactory
                .getApiService()
                .getReportDetail(id)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<ReportDetailResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {

                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull ReportDetailResp value) {
                        mView.getReportDetail(value);
                    }
                });
    }
}
