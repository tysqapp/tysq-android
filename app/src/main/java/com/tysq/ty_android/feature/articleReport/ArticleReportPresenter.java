package com.tysq.ty_android.feature.articleReport;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import request.ArticleReportReq;

public final class ArticleReportPresenter extends BasePresenter<ArticleReportView> {
    @Inject
    public ArticleReportPresenter(ArticleReportView view) {
        super(view);
    }

    public void postArticleReport(String id, String reportType, String note) {
        RetrofitFactory
                .getApiService()
                .postArticleReport(new ArticleReportReq(id, reportType, note))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onPostArticleReport();
                    }
                });
    }
}
