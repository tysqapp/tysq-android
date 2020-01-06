package com.tysq.ty_android.feature.articleCollect;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import request.ArticleCollectReq;
import response.article.ArticleCollectResp;

public final class ArticleCollectPresenter extends BasePresenter<ArticleCollectView> {
    @Inject
    public ArticleCollectPresenter(ArticleCollectView view) {
        super(view);
    }

    public void getArticleCollect(int accountId, int start, int pageSize, boolean isFirst) {
        RetrofitFactory.getApiService()
                .getArticleCollectList(accountId, start, pageSize)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<ArticleCollectResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onGetArticleCollectError(isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull ArticleCollectResp value) {
                        mView.onGetArticleCollect(isFirst,
                                value.getCollects(),
                                value.getTotalNum());
                    }
                });
    }

    public void putCancelCollect(String articleId) {
        RetrofitFactory
                .getApiService()
                .putArticleCollect(new ArticleCollectReq(articleId, Constant.CollectType.CANCEL))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                        mView.onCancelCollectError(articleId);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.hideDialog();
                        mView.onCancelCollect(articleId);
                    }
                });
    }
}
