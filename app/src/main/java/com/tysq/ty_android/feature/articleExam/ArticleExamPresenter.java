package com.tysq.ty_android.feature.articleExam;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.article.ReviewArticleListResp;

public final class ArticleExamPresenter extends BasePresenter<ArticleExamView> {
    @Inject
    public ArticleExamPresenter(ArticleExamView view) {
        super(view);
    }

    public void loadExamArticle(int start,
                                int pageSize,
                                boolean isFirst) {

        RetrofitFactory.getApiService()
                .getReviewArticles(start, pageSize, -1)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<ReviewArticleListResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadExamArticleError(isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull ReviewArticleListResp value) {
                        mView.onLoadExamArticle(value.getReviewArticles(),
                                value.getArticlesNum(), isFirst);
                    }
                });

    }
}
