package com.tysq.ty_android.feature.myReview;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.myReview.adapter.MyReviewAdapter;
import com.tysq.ty_android.feature.myReview.di.DaggerMyReviewComponent;
import com.tysq.ty_android.feature.myReview.di.MyReviewModule;
import com.tysq.ty_android.base.SimpleLoadMoreFragment;
import com.tysq.ty_android.local.sp.UserCache;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.Override;
import java.util.List;

import eventbus.MyReviewChangeEvent;
import response.MyCommentListResp;
import response.common.TitleCountVO;

/**
 * author       : frog
 * time         : 2019/5/26 下午2:44
 * desc         : 我的评论
 * version      : 1.3.0
 */

public final class MyReviewFragment
        extends SimpleLoadMoreFragment<MyReviewPresenter, MyCommentListResp.CommentInfoBean>
        implements MyReviewView, MyReviewListener {

    public static final String TAG = "MyReviewFragment";

    private final TitleCountVO mTitleCountVO = new TitleCountVO();

    public static MyReviewFragment newInstance() {
        return newInstance(UserCache.getDefault().getAccountId(),
                true,
                true,
                true);
    }

    public static MyReviewFragment newInstance(int userId,
                                               boolean isNeedHeader,
                                               boolean isNeedRefresh,
                                               boolean isNeedSwipe) {
        Bundle args = new Bundle();
        args.putInt(USER_ID, userId);
        args.putBoolean(IS_NEED_HEADER, isNeedHeader);
        args.putBoolean(IS_NEED_REFRESH, isNeedRefresh);
        args.putBoolean(IS_NEED_SWIPE, isNeedSwipe);

        MyReviewFragment fragment = new MyReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void registerDagger() {
        DaggerMyReviewComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .myReviewModule(new MyReviewModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new MyReviewAdapter(this,
                getContext(),
                mTitleCountVO,
                this,
                mData,
                isNeedHeader,
                isNeedSwipe);
    }

    @Override
    protected boolean isNeedEmpty() {
        return true;
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.loadMyReview(userId, isFirst, start, pageSize);
    }

    @Override
    public void onLoadMyReviewError(boolean isFirst) {
        onHandleError(isFirst);
    }

    @Override
    public void onLoadMyReview(boolean isFirst,
                               int totalNum,
                               List<MyCommentListResp.CommentInfoBean> commentInfo) {

        mTitleCountVO.setCount(totalNum);

        onHandleResponseData(commentInfo, isFirst);

    }

    @Override
    public void onDeleteComment(String commentId) {
        deleteCommentViaId(commentId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteComment(MyReviewChangeEvent event) {
        deleteCommentViaId(event.getCommentId());
    }

    /**
     * 删除评论，通过id
     *
     * @param commentId 删除的评论id
     */
    private void deleteCommentViaId(String commentId) {
        int pos = -1;

        for (int i = 0; i < mData.size(); i++) {
            MyCommentListResp.CommentInfoBean data = mData.get(i);
            if (data.getId().equals(commentId)) {
                pos = i;
                break;
            }
        }

        if (pos == -1) {
            return;
        }

        // 数据源移除
        mData.remove(pos);
        mTitleCountVO.reduceTotal();
        size -= 1;
        if (size <= 0) {
            size = 0;
        }

        // 如果数量到达0，则重新load一下
        if (mData.size() <= 0 || mTitleCountVO.getCount() <= 0 || size <= 0) {
            mBaseAdapter.onLoading();
            loadData(size, PAGE_SIZE, true);
            return;
        }

        pos += requestRefresh() ? 1 : 0;
        // 头部
        pos += 1;
        mBaseAdapter.notifyItemRemoved(pos);

    }

    @Override
    protected int getEmptyView() {
        return R.layout.blank_empty_my_review;
    }

    @Override
    public void deleteReviewItem(String commentId) {
        showDialog();
        mPresenter.deleteComment(commentId);
    }

    @Override
    public int getTitleId() {
        return R.string.my_review_title;
    }
}
