package com.tysq.ty_android.feature.myArticle;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.article.MyArticleResp;

public final class MyArticlePresenter extends BasePresenter<MyArticleView> {
    @Inject
    public MyArticlePresenter(MyArticleView view) {
        super(view);
    }

    public void loadArticle(int accountId, int status, int start, int loadSize, boolean isFirst) {

        RetrofitFactory
                .getApiService()
                .getMyArticleList(accountId,status, start, loadSize)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<MyArticleResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadArticleError(isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull MyArticleResp value) {
                        mView.onLoadArticle(isFirst, value.getArticlesInfo(), value.getTotalNum());
                    }
                });

    }
}
