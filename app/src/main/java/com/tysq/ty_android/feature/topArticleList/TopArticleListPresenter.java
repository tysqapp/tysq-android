package com.tysq.ty_android.feature.topArticleList;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.TopArticleResp;

public final class TopArticleListPresenter extends BasePresenter<TopArticleListView> {
    @Inject
    public TopArticleListPresenter(TopArticleListView view) {
        super(view);
    }

    public void getTopArticleList(int selTopIndex, int selSubIndex) {
        RetrofitFactory
                .getApiService()
                .getTopArticleList(selTopIndex, selSubIndex)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<TopArticleResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull TopArticleResp value) {
                        mView.onLoadTopArticleList(value.getTopArticleBeanList());
                    }
                });
    }
}
