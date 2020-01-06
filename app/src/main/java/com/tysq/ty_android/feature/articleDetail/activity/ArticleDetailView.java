package com.tysq.ty_android.feature.articleDetail.activity;

import com.bit.view.IView;

import response.rank.JudgementResp;

public interface ArticleDetailView extends IView {
    void onGetJudgement(JudgementResp value, int reviewPos);

    void onCollectError();

    void onCollect();

    void onGetArticleCodeFail();

    void onGetArticleCode(String captchaId, String captchaBase64);

    void onPostArticleCaptcha();

    void onPutArticleReviewError();

    void onPutArticleReview();

    void onPutArticleHideSuccess(int state);

    void onPutTopArticleSuccess(int position);
}
