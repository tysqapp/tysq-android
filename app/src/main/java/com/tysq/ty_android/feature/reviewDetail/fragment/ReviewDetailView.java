package com.tysq.ty_android.feature.reviewDetail.fragment;

import com.bit.view.IView;

import response.article.ArticleReviewResp;
import response.rank.JudgementResp;

public interface ReviewDetailView extends IView {
    void onLoadReviewDetailError(boolean isFirst);

    void onLoadReviewDetail(boolean isFirst,
                            ArticleReviewResp.ArticleCommentsBean articleCommentsBean);

    void onDeleteComment(int position,
                         String commentId,
                         String topId,
                         boolean isTop);

    void onGetJudgement(JudgementResp value,
                        String tag,
                        String receiverName,
                        String articleId,
                        int receiverId,
                        String topCommentId,
                        String commentId,
                        int position);
}
