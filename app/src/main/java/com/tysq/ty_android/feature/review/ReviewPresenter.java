package com.tysq.ty_android.feature.review;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.feature.articleDetail.config.ArticleConstants;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import java.util.List;

import javax.inject.Inject;

import request.ReviewReq;
import response.article.ReviewResp;
import vo.article.ArticleDetailVO;

public final class ReviewPresenter extends BasePresenter<ReviewView> {
    @Inject
    public ReviewPresenter(ReviewView view) {
        super(view);
    }

    public void publishComment(String articleId,
                               int atAccountId,
                               String content,
                               String parentId,
                               String fatherId,
                               List<Integer> imgList) {

        ReviewReq reviewReq = new ReviewReq();
        reviewReq.setArticleId(articleId);
        reviewReq.setAtAccountId(atAccountId);
        reviewReq.setContent(content);
        reviewReq.setParentId(parentId);
        reviewReq.setFatherId(fatherId);
        reviewReq.setImageId(imgList);

        RetrofitFactory
                .getApiService()
                .postComment(reviewReq)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<ReviewResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull ReviewResp value) {
                        if (value.getTopComment() != null) {
                            mView.onPublishComment(
                                    new ArticleDetailVO<>(ArticleConstants.REVIEW_TOP_ITEM,
                                            value.getTopComment()),
                                    value.getLimitScore()
                            );
                        } else if (value.getSubComment() != null) {

                            value.getSubComment().setArticleId(articleId);

                            mView.onPublishComment(
                                    new ArticleDetailVO<>(ArticleConstants.REVIEW_SUB_ITEM,
                                            value.getSubComment()),
                                    value.getLimitScore()
                            );
                        } else {
                            mView.hideDialog();
                        }
                    }
                });

    }
}
