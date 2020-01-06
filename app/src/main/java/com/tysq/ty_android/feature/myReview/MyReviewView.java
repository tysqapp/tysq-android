package com.tysq.ty_android.feature.myReview;

import com.bit.view.IView;

import java.util.List;

import response.MyCommentListResp;

public interface MyReviewView extends IView {

    void onLoadMyReviewError(boolean isFirst);

    void onLoadMyReview(boolean isFirst,
                        int totalNum,
                        List<MyCommentListResp.CommentInfoBean> commentInfo);

    void onDeleteComment(String commentId);
}
