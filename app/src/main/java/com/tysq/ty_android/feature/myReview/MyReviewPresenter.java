package com.tysq.ty_android.feature.myReview;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import request.DeleteCommentReq;
import response.MyCommentListResp;

public final class MyReviewPresenter extends BasePresenter<MyReviewView> {
    @Inject
    public MyReviewPresenter(MyReviewView view) {
        super(view);
    }

    public void loadMyReview(int accountId, boolean isFirst, int start, int size) {
        RetrofitFactory
                .getApiService()
                .getMyCommentList(accountId, start, size)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<MyCommentListResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadMyReviewError(isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull MyCommentListResp value) {
                        List<MyCommentListResp.CommentInfoBean> commentInfo = value.getCommentInfo();
                        if (commentInfo == null) {
                            commentInfo = new ArrayList<>();
                        }

                        mView.onLoadMyReview(isFirst, value.getTotalNum(), commentInfo);
                    }
                });
    }

    public void deleteComment(String commentId) {
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
                        mView.hideDialog();
                        mView.onDeleteComment(commentId);
                    }
                });
    }
}
