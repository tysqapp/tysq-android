package com.tysq.ty_android.feature.articleDetail.activity;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import request.ArticleCaptchaReq;
import request.ArticleCollectReq;
import request.ArticleReviewReq;
import request.HideArticleReq;
import request.TopArticleReq;
import response.login.RespCaptcha;
import response.rank.JudgementResp;

public final class ArticleDetailPresenter extends BasePresenter<ArticleDetailView> {

    @Inject
    public ArticleDetailPresenter(ArticleDetailView view) {
        super(view);
    }

    public void getJudgement(String articleId,
                             final int reviewPos) {
        RetrofitFactory
                .getApiService()
                .getJudgement(Constant.JudgementType.COMMENT, articleId, articleId)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<JudgementResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull JudgementResp value) {
                        mView.hideDialog();
                        mView.onGetJudgement(value, reviewPos);
                    }
                });
    }

    public void collectArticle(String articleId,
                               int collectState) {
        RetrofitFactory
                .getApiService()
                .putArticleCollect(new ArticleCollectReq(articleId, collectState))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onCollectError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onCollect();
                    }
                });
    }

    public void getArticleCode(int width, int height) {
        RetrofitFactory
                .getApiService()
                .getVerifyCode(Constant.CaptchaType.ARTICLE, width, height)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<RespCaptcha>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onGetArticleCodeFail();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull RespCaptcha value) {
                        mView.onGetArticleCode(value.getCaptchaId(), value.getCaptchaBase64());
                    }
                });

    }

    public void postArticleCaptcha(String captchaId, String code) {
        RetrofitFactory
                .getApiService()
                .postArticleCaptcha(new ArticleCaptchaReq(captchaId, code))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onPostArticleCaptcha();
                    }
                });
    }

    public void putArticleReview(String articleId,
                                 boolean isPass,
                                 String reason) {
        ArticleReviewReq articleReviewReq = new ArticleReviewReq();
        articleReviewReq.setArticleId(articleId);
        articleReviewReq.setStatus(isPass ?
                Constant.ReviewStatus.PASS :
                Constant.ReviewStatus.REFUSE);
        articleReviewReq.setReason(reason);

        RetrofitFactory
                .getApiService()
                .putArticleReview(articleReviewReq)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onPutArticleReviewError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onPutArticleReview();
                    }
                });
    }

    public void putHideArticle(String articleId, int state) {
        RetrofitFactory
                .getApiService()
                .putHideArticle(articleId, new HideArticleReq(state))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onPutArticleHideSuccess(state);
                    }
                });
    }

    public void putTopArticlePosition(String articleId, int position) {
        RetrofitFactory
                .getApiService()
                .putTopArticle(articleId, new TopArticleReq(position))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onPutTopArticleSuccess(position);
                    }
                });
    }
}
