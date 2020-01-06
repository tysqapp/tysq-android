package com.tysq.ty_android.feature.reviewDetail.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.fragment.BitLoadMoreFragment;
import com.jerry.image_watcher.ImageWatcherActivity;
import com.jerry.image_watcher.model.WatchImageVO;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.articleDetail.dialog.UserAdminDialog;
import com.tysq.ty_android.feature.articleDetail.listener.ArticleDetailListener;
import com.tysq.ty_android.feature.personalHomePage.PersonalHomePageActivity;
import com.tysq.ty_android.feature.rank.tip.RankTipDialog;
import com.tysq.ty_android.feature.review.ReviewActivity;
import com.tysq.ty_android.feature.reviewDetail.adapter.ReviewDetailAdapter;
import com.tysq.ty_android.feature.reviewDetail.di.DaggerReviewDetailComponent;
import com.tysq.ty_android.feature.reviewDetail.di.ReviewDetailModule;
import com.tysq.ty_android.feature.reviewDetail.listener.ReviewDetailListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import eventbus.DetailReviewAddSyncEvent;
import eventbus.DetailReviewDelSyncEvent;
import eventbus.LoginStateChangeEvent;
import eventbus.ReviewAddEvent;
import response.article.ArticleReviewResp;
import response.rank.JudgementResp;
import vo.article.ArticleAudioVO;
import vo.article.ArticleVideoVO;

/**
 * author       : frog
 * time         : 2019/5/23 下午2:58
 * desc         : 评论详情
 * version      : 1.3.0
 */

public final class ReviewDetailFragment
        extends BitLoadMoreFragment<ReviewDetailPresenter>
        implements ReviewDetailView, ArticleDetailListener {

    public static final int SIZE = 20;

    public static final boolean IS_NEED_REFRESH = true;

    public static final String TAG = "ReviewDetailFragment";

    public static final String ARTICLE_ID = "article_id";
    public static final String REVIEW_ID = "review_id";
    public static final String IS_CAN_DELETE = "is_can_delete";
    public static final String IS_CAN_FORBID = "is_can_forbid";

    private ReviewDetailAdapter mAdapter;

    private UserAdminDialog mUserAdminDialog;

    private String mArticleId;
    private String mReviewId;
    private boolean mIsCanDelete;
    private boolean mIsCanForbid;
    private long mStartTime;

    private ArticleReviewResp.ArticleCommentsBean mTopData;
    private final List<ArticleReviewResp.ArticleCommentsBean.ReplyBean> mSubData = new ArrayList<>();

    public static ReviewDetailFragment newInstance(String articleId,
                                                   String reviewId,
                                                   boolean isCanDelete,
                                                   boolean isCanForbid) {
        Bundle args = new Bundle();
        args.putString(ARTICLE_ID, articleId);
        args.putString(REVIEW_ID, reviewId);
        args.putBoolean(IS_CAN_DELETE, isCanDelete);
        args.putBoolean(IS_CAN_FORBID, isCanForbid);

        ReviewDetailFragment fragment = new ReviewDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void registerDagger() {
        DaggerReviewDetailComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .reviewDetailModule(new ReviewDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initArgs(Bundle arguments) {
        mArticleId = arguments.getString(ARTICLE_ID);
        mReviewId = arguments.getString(REVIEW_ID);
        mIsCanDelete = arguments.getBoolean(IS_CAN_DELETE);
        mIsCanForbid = arguments.getBoolean(IS_CAN_FORBID);
    }

    @Override
    protected boolean requestRefresh() {
        return IS_NEED_REFRESH;
    }

    @Override
    public void getLoadMoreData() {
        loadData(false);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mAdapter = new ReviewDetailAdapter(this, getContext());
        mAdapter.setListener(this);
        return mAdapter;
    }

    @Override
    public void getFirstData(int type) {
        mStartTime = 0;

        loadData(true);
    }

    private void loadData(boolean isFirst) {
        mPresenter.loadReviewDetail(isFirst,
                mIsCanDelete,
                mIsCanForbid,
                mArticleId,
                mReviewId,
                mStartTime,
                SIZE);
    }

    @Override
    public void onDeleteCommentItem(View view,
                                    int position,
                                    String commentId,
                                    String topId,
                                    boolean isTop) {
        hideDialog();
        mPresenter.deleteComment(position, commentId, topId, isTop);
    }

    /**
     * @param view         视图
     * @param position     位置
     * @param articleId    文章id
     * @param receiverId   被评论者id
     * @param receiverName 被评论者名字
     * @param commentId    该条评论的id
     * @param topCommentId 一级评论的id
     */
    @Override
    public void onReplyComment(View view,
                               int position,
                               String articleId,
                               int receiverId,
                               String receiverName,
                               String commentId,
                               String topCommentId) {

        showDialog();

        mPresenter.getJudgement(TAG,
                receiverName,
                articleId,
                receiverId,
                topCommentId,
                commentId,
                position);

    }

    @Override
    public void onAudioClick(View v, int adapterPosition, ArticleAudioVO articleAudioVO) {
        // 2019/6/11 空实现
    }

    @Override
    public void onSeekTo(View v, int adapterPosition, ArticleAudioVO articleAudioVO) {
        // 2019/6/11 空实现
    }

    @Override
    public void onShowUserAdmin(int commentatorId,
                                String iconUrl,
                                String commentatorName,
                                int lv) {

//        // 不能禁止，则返回
//        if (!mIsCanForbid) {
//            return;
//        }
//
//        getUserAdminDialog()
//                .setInfo(commentatorId,
//                        iconUrl,
//                        commentatorName,
//                        lv)
//                .show(this);

        PersonalHomePageActivity.startActivity(getContext(), commentatorId);

    }

    @Override
    public void onShowCommentImage(ArrayList<WatchImageVO> imageList, int pos) {
        ImageWatcherActivity.startActivity(getContext(), imageList, pos);
    }

    @Override
    public void onShowDetailImage(int pos) {
        // 2019-09-16 空实现
    }

    @Override
    public void downloadVideo(ArticleVideoVO articleVideoVO) {
        // 2019-09-16 空实现
    }

    @Override
    public void onShowRewardTip() {
        //2019-11-8 空实现
    }

    @Override
    public void onShowRewardList() {
        //2019-11-8 空实现
    }

    @Override
    protected int getInitRegister() {
        return EVENT_BUS | BUTTER_KNIFE;
    }

    @Override
    public void onLoadReviewDetailError(boolean isFirst) {
        if (isFirst) {
            mBaseAdapter.onError();
        } else {
            mBaseAdapter.setLoadError();
        }
    }

    @Override
    public void onLoadReviewDetail(boolean isFirst,
                                   ArticleReviewResp.ArticleCommentsBean articleCommentsBean) {

        if (articleCommentsBean.getReply() != null && articleCommentsBean.getReply().size() > 0) {
            mStartTime = articleCommentsBean
                    .getReply().get(articleCommentsBean.getReply().size() - 1).getTime();
        }

        if (isFirst) {

            FragmentActivity activity = getActivity();
            if (activity instanceof ReviewDetailListener) {
                ReviewDetailListener listener = (ReviewDetailListener) activity;
                listener.onSetInfo(articleCommentsBean.getCommentatorName(),
                        articleCommentsBean.getCommentatorId(),
                        articleCommentsBean.getId());
            }

            mSubData.clear();
            if (articleCommentsBean.getReply() != null) {
                mSubData.addAll(articleCommentsBean.getReply());
            }

            mTopData = articleCommentsBean;

            mAdapter.setTopData(mTopData);
            mAdapter.setSubData(mSubData);

            mBaseAdapter.onSuccess();

            if (articleCommentsBean.getReply() == null
                    || articleCommentsBean.getReply().size() < SIZE) {
                mBaseAdapter.setNoMore();
            }

            return;
        }

        if (articleCommentsBean.getReply() != null &&
                articleCommentsBean.getReply().size() > 0) {
            int oldSize = mSubData.size();
            mSubData.addAll(articleCommentsBean.getReply());
            mBaseAdapter.notifyItemRangeInserted(
                    ReviewDetailAdapter.HEAD_COUNT + oldSize - 1,
                    articleCommentsBean.getReply().size());
        }

        if (articleCommentsBean.getReply() == null
                || articleCommentsBean.getReply().size() < SIZE) {
            mBaseAdapter.setNoMore();
        }

    }

    @Override
    public void onDeleteComment(int position, String commentId, String topId, boolean isTop) {
        hideDialog();

        ToastUtils.show(getString(R.string.article_detail_delete_suc));

        int realPos = IS_NEED_REFRESH ? position - 1 : position;

        sendDeleteSync(commentId, topId, isTop);

        // 一级评论被删除，直接退出当前页
        if (isTop && getActivity() != null) {
            getActivity().finish();
            return;
        }

        // 二级评论，直接移除即可
        mSubData.remove(realPos - ReviewDetailAdapter.HEAD_COUNT);
        mBaseAdapter.notifyItemRemoved(position);
        updateReviewCount();

    }

    @Override
    public void onGetJudgement(JudgementResp value,
                               String tag,
                               String receiverName,
                               String articleId,
                               int receiverId,
                               String topCommentId,
                               String commentId,
                               int position) {

        if (value.isIsSatisfy()) {
            ReviewActivity.startActivity(getContext(),
                    tag,
                    receiverName,
                    articleId,
                    receiverId,
                    topCommentId,
                    commentId,
                    position);
        } else {
            RankTipDialog
                    .newInstance(value.getLimitScore(), Constant.JudgementType.COMMENT)
                    .show(this);
        }


    }

    /**
     * 同步删除 至 文章详情
     */
    private void sendDeleteSync(String commentId,
                                String parentId,
                                boolean isTop) {
        // 让 文章详情 添加 新增的评论
        DetailReviewDelSyncEvent syncEvent = new DetailReviewDelSyncEvent();
        syncEvent.setCommentId(commentId);
        syncEvent.setParentId(parentId);
        syncEvent.setTop(isTop);

        EventBus.getDefault().post(syncEvent);
    }

    /**
     * 添加评论
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddReview(ReviewAddEvent event) {

        if (!event.getFrom().equals(TAG)) {
            return;
        }

        if (event.getPos() == -1) {
            return;
        }

        // 不是同篇文章id，直接忽略
        if (!event.getArticleId().equals(mArticleId)) {
            return;
        }

        int realPos = IS_NEED_REFRESH ? event.getPos() - 1 : event.getPos();

        Log.i(TAG, "onAddReview: " + event.getPos());

        ArticleReviewResp.ArticleCommentsBean.ReplyBean replyData =
                (ArticleReviewResp.ArticleCommentsBean.ReplyBean) event.getArticleDetailVO().getData();

        // 是否要整体刷新，当二级评论为0时，进行整体刷新，因为数量问题
        boolean isRefreshAll = false;
        if (mSubData.size() <= 0) {
            isRefreshAll = true;
        }
        mSubData.add(realPos, replyData);

        if (isRefreshAll) {
            mBaseAdapter.notifyDataSetChanged();
        } else {
            mBaseAdapter.notifyItemInserted(event.getPos() + ReviewDetailAdapter.HEAD_COUNT);
        }

        updateReviewCount();

        // 让 文章详情 添加 新增的评论
        DetailReviewAddSyncEvent syncEvent = new DetailReviewAddSyncEvent();
        syncEvent.setParentId(replyData.getParentId());
        syncEvent.setReviewData(replyData);

        EventBus.getDefault().post(syncEvent);

    }

    /**
     * 刷新全部评论的条数
     */
    private void updateReviewCount() {
        int reviewTitleIndex = IS_NEED_REFRESH ?
                ReviewDetailAdapter.REVIEW_TITLE + 1 :
                ReviewDetailAdapter.REVIEW_TITLE;

        mBaseAdapter.notifyItemChanged(reviewTitleIndex);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSt(LoginStateChangeEvent loginStateChangeEvent) {
        mBaseAdapter.notifyDataSetChanged();
    }

    private UserAdminDialog getUserAdminDialog() {
        if (mUserAdminDialog == null) {
            mUserAdminDialog = UserAdminDialog.newInstance();
        }
        return mUserAdminDialog;
    }

}
