package com.tysq.ty_android.feature.reviewDetail.fragment;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxObservableSubscriber;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import request.DeleteCommentReq;
import response.article.ArticleReviewResp;
import response.rank.JudgementResp;

public final class ReviewDetailPresenter extends BasePresenter<ReviewDetailView> {

    @Inject
    public ReviewDetailPresenter(ReviewDetailView view) {
        super(view);
    }

    public void loadReviewDetail(final boolean isFirst,
                                 final boolean isCanDelete,
                                 final boolean isCanForbid,
                                 String articleId,
                                 String commentId,
                                 long startTime,
                                 int size) {

        RetrofitFactory
                .getApiService()
                .getReviewList(articleId, commentId, startTime, size)
                .compose(RxParser.handleObservableDataResult())
                .doOnNext(new Consumer<ArticleReviewResp>() {
                    @Override
                    public void accept(ArticleReviewResp articleReviewResp) throws Exception {
                        if (articleReviewResp != null
                                && articleReviewResp.getArticleComments() != null
                                && articleReviewResp.getArticleComments().size() > 0) {


                            ArticleReviewResp.ArticleCommentsBean articleComments
                                    = articleReviewResp.getArticleComments().get(0);

                            articleComments.setCanDelete(isCanDelete);
                            articleComments.setCanForbid(isCanForbid);

                            if (articleComments.getReply() != null) {
                                for (ArticleReviewResp.ArticleCommentsBean.ReplyBean replyBean
                                        : articleComments.getReply()) {
                                    replyBean.setCanDelete(isCanDelete);
                                    replyBean.setArticleId(articleComments.getArticleId());
                                }
                            }
                        }
                    }
                })
                .subscribe(new RxObservableSubscriber<ArticleReviewResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadReviewDetailError(isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull ArticleReviewResp value) {
                        if (value.getArticleComments().size() <= 0) {
                            mView.onLoadReviewDetailError(isFirst);
                            return;
                        }

                        ArticleReviewResp.ArticleCommentsBean articleCommentsBean
                                = value.getArticleComments().get(0);

                        mView.onLoadReviewDetail(isFirst, articleCommentsBean);
                    }
                });

    }

    /**
     * 删除评论
     */
    public void deleteComment(final int position,
                              final String commentId,
                              final String topId,
                              final boolean isTop) {

        RetrofitFactory
                .getApiService()
                .deleteArticleComment(new DeleteCommentReq(commentId))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.onDeleteComment(position, commentId, topId, isTop);
                    }
                });

    }

    public void getJudgement(final String tag,
                             final String receiverName,
                             final String articleId,
                             final int receiverId,
                             final String topCommentId,
                             final String commentId,
                             final int position) {
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
                        mView.onGetJudgement(value,
                                tag,
                                receiverName,
                                articleId,
                                receiverId,
                                topCommentId,
                                commentId,
                                position);
                    }
                });
    }
}
