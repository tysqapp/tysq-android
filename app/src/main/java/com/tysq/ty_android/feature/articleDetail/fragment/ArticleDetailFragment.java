package com.tysq.ty_android.feature.articleDetail.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.abc.download.DownloadUtils;
import com.abc.lib_log.JLogUtils;
import com.abc.lib_utils.toast.ToastUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.config.BitManager;
import com.bit.view.fragment.BitLoadMoreFragment;
import com.jerry.image_watcher.ImageWatcherActivity;
import com.jerry.image_watcher.model.WatchImageVO;
import com.jerry.media.controller.IJerryAudioController;
import com.jerry.media.controller.JerryAudioController;
import com.jerry.media.model.MediaInfo;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import com.tysq.ty_android.feature.articleDetail.adapter.ArticleDetailAdapter;
import com.tysq.ty_android.feature.articleDetail.config.ArticleConstants;
import com.tysq.ty_android.feature.articleDetail.di.ArticleDetailModule;
import com.tysq.ty_android.feature.articleDetail.di.DaggerArticleDetailComponent;
import com.tysq.ty_android.feature.articleDetail.dialog.UserAdminDialog;
import com.tysq.ty_android.feature.articleDetail.listener.ArticleDetailActivityListener;
import com.tysq.ty_android.feature.articleDetail.listener.ArticleDetailListener;
import com.tysq.ty_android.feature.articleDetail.listener.ArticleRewardListener;
import com.tysq.ty_android.feature.articleDetail.tip.RewardArticleDetailDialog;
import com.tysq.ty_android.feature.articleDetail.tip.RewardArticleDialog;
import com.tysq.ty_android.feature.editArticle.EditArticleActivity;
import com.tysq.ty_android.feature.homePage.HomePageFragment;
import com.tysq.ty_android.feature.personalHomePage.PersonalHomePageActivity;
import com.tysq.ty_android.feature.rank.tip.RankTipActivity;
import com.tysq.ty_android.feature.rank.tip.RankTipDialog;
import com.tysq.ty_android.feature.review.ReviewActivity;
import com.tysq.ty_android.feature.rewardList.RewardListActivity;
import com.tysq.ty_android.local.sp.NetCache;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.net.config.NetConfig;
import com.abc.lib_multi_download.jerry.JerryDownload;
import com.zinc.libpermission.annotation.Permission;
import com.zinc.libpermission.annotation.PermissionCanceled;
import com.zinc.libpermission.annotation.PermissionDenied;
import com.zinc.libpermission.bean.CancelInfo;
import com.zinc.libpermission.bean.DenyInfo;
import com.zinc.libpermission.utils.JPermissionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import eventbus.DeleteArticleEvent;
import eventbus.DetailReviewAddSyncEvent;
import eventbus.DetailReviewDelSyncEvent;
import eventbus.DownloadAddEvent;
import eventbus.HideArticleEvent;
import eventbus.LoginStateChangeEvent;
import eventbus.MyReviewChangeEvent;
import eventbus.ReviewAddEvent;
import eventbus.TopArticlePositionEvent;
import eventbus.UpdateArticleInfoEvent;
import response.article.ArticleDetailResp;
import response.article.ArticleDownloadVideoResp;
import response.article.ArticleReviewResp;
import response.article.RecommendArticleResp;
import response.article.RewardArticleResp;
import response.article.RewardListResp;
import response.common.TitleCountVO;
import response.permission.PermissionResp;
import response.rank.JudgementResp;
import vo.article.ArticleAudioVO;
import vo.article.ArticleDetailVO;
import vo.article.ArticleDividerVO;
import vo.article.ArticleMoreVO;
import vo.article.ArticleVideoVO;

/**
 * author       : frog
 * time         : 2019/5/24 下午3:32
 * desc         : 文章详情
 * version      : 1.3.0
 */

public final class ArticleDetailFragment
        extends BitLoadMoreFragment<ArticleDetailPresenter>
        implements ArticleDetailView,
        ArticleDetailListener,
        ArticleRewardListener {

    public static final String TAG = "ArticleDetailFragment";

    private static final int LOAD_SIZE = 10;
    private static final boolean IS_NEED_REFRESH = false;

    public static final String ARTICLE_ID = "ARTICLE_ID";
    public static final String COMMENT_ID = "COMMENT_ID";

    private String mArticleId;
    private String mCommentId;
    //置顶文章顺序
    private int mTopPosition;

    private final ArrayList<ArticleDetailVO> mData = new ArrayList<>();
    private final ArrayList<WatchImageVO> mImageList = new ArrayList<>();

    private ArticleDetailResp.ArticleInfoBean mArticleInfo;
    private long mStartTime;

    private IJerryAudioController jerryAudioController;

    private UserAdminDialog mUserAdminDialog;

    private RewardArticleDialog mRewardArticleDialog;

    private RewardArticleDetailDialog mRewardArticleDetailDialog;

    private ArticleDetailAdapter articleDetailAdapter;

    // 加载是否完毕
    private boolean mIsLoadOver = false;

    private PermissionResp mPermissionInfo;

    public static ArticleDetailFragment newInstance(String articleId,
                                                    String commentId) {
        Bundle args = new Bundle();
        args.putString(ARTICLE_ID, articleId);
        args.putString(COMMENT_ID, commentId);

        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        mArticleId = arguments.getString(ARTICLE_ID);
        mCommentId = arguments.getString(COMMENT_ID);
    }

    @Override
    protected void initData() {
        super.initData();

        jerryAudioController = new JerryAudioController(getContext());

        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (mArticleInfo == null) {
                    if (getActivity() instanceof ArticleDetailActivityListener) {
                        ArticleDetailActivityListener listener = (ArticleDetailActivityListener) getActivity();
                        listener.onSetTitle("", 0);
                    }
                    return;
                }

                View firstView = recyclerView.getLayoutManager().findViewByPosition(0);

                if (firstView == null) {
                    if (getActivity() instanceof ArticleDetailActivityListener) {
                        ArticleDetailActivityListener listener = (ArticleDetailActivityListener) getActivity();
                        listener.onSetTitle(mArticleInfo.getTitle(), 1);
                    }
                    return;
                }

                float top = -firstView.getTop();
                float height = (int) (firstView.getHeight() * 0.85f);

                float percent = top / height;

                if (getActivity() instanceof ArticleDetailActivityListener) {
                    ArticleDetailActivityListener listener = (ArticleDetailActivityListener) getActivity();
                    listener.onSetTitle(mArticleInfo.getTitle(), percent);
                }
            }
        });
    }

    @Override
    protected void registerDagger() {
        DaggerArticleDetailComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .articleDetailModule(new ArticleDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void getLoadMoreData() {
        mPresenter.loadReviewMore(mPermissionInfo, mArticleId, mStartTime, LOAD_SIZE);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        articleDetailAdapter = new ArticleDetailAdapter(this, getContext(), mData);
        articleDetailAdapter.setListener(this);

        return articleDetailAdapter;
    }

    public void reload() {
        mBaseAdapter.onLoading();
        getFirstData(LoadType.CUSTOM);
    }

    @Override
    public void getFirstData(int type) {
        mStartTime = 0;

        mPresenter.loadArticleInfo(mArticleId, mStartTime, LOAD_SIZE);
    }

    @Override
    protected boolean requestRefresh() {
        return IS_NEED_REFRESH;
    }

    @Override
    public void onLoadArticleInfoError() {
        mBaseAdapter.onError();
    }

    @Override
    public void onLoadArticleInfo(ArticleDetailResp.ArticleInfoBean articleInfo,
                                  List<ArticleDetailVO> data,
                                  int reviewSize,
                                  int topReviewSize,
                                  long startTime,
                                  boolean isDelete) {

        mArticleInfo = articleInfo;

        mData.clear();
        mData.addAll(data);

        mTopPosition = articleInfo.getTopPosition();

        if (mArticleInfo != null) {
            if (getActivity() instanceof ArticleDetailActivityListener) {
                ArticleDetailActivityListener listener = (ArticleDetailActivityListener) getActivity();
                listener.onGetIsHideArticle(mArticleInfo.getStatus());
            }
        }

        // 已经被删除
        if (isDelete) {

            mBaseAdapter.setIsOpenLoadMore(false);
            mBaseAdapter.onSuccess();

            FragmentActivity activity = getActivity();
            if (activity instanceof ArticleDetailActivityListener) {
                ArticleDetailActivityListener listener = (ArticleDetailActivityListener) activity;
                listener.onHideControlBar();
            }

            return;
        }

        // 提示积分
        if (mArticleInfo != null && mArticleInfo.getLimitScore() > 0) {
            String tip = getString(R.string.article_detail_read_use_score);
            tip = String.format(tip, mArticleInfo.getLimitScore());
            ToastUtils.show(tip);
        }

        if (topReviewSize < LOAD_SIZE) {
            mBaseAdapter.setNoMore();
        } else {
            mBaseAdapter.setLoadComplete();
        }

        mStartTime = startTime;

        mBaseAdapter.onSuccess();

        FragmentActivity activity = getActivity();
        if (activity instanceof ArticleDetailActivityListener) {
            ArticleDetailActivityListener listener = (ArticleDetailActivityListener) activity;
            listener.onSetArticleInfo(articleInfo.getAccount(), reviewSize);
            listener.onSetCollectState(articleInfo.isCollectStatus());
        }

        if (!TextUtils.isEmpty(mCommentId)) {
            int scrollToPosition = -1;
            for (int i = 0; i < mData.size(); i++) {
                ArticleDetailVO articleDetailVO = mData.get(i);

                if (articleDetailVO.getType() == ArticleConstants.REVIEW_TOP_ITEM) {
                    ArticleReviewResp.ArticleCommentsBean topItem =
                            (ArticleReviewResp.ArticleCommentsBean) articleDetailVO.getData();
                    if (topItem.getId().equals(mCommentId)) {
                        scrollToPosition = i;
                        break;
                    }
                } else if (articleDetailVO.getType() == ArticleConstants.REVIEW_SUB_ITEM) {
                    ArticleReviewResp.ArticleCommentsBean.ReplyBean subItem =
                            (ArticleReviewResp.ArticleCommentsBean.ReplyBean) articleDetailVO.getData();
                    if (subItem.getId().equals(mCommentId)) {
                        scrollToPosition = i;
                        break;
                    }
                }
            }

            LinearLayoutManager layoutManager
                    = (LinearLayoutManager) mRecycleView.getLayoutManager();

            if (scrollToPosition != -1) {
                layoutManager.scrollToPositionWithOffset(scrollToPosition, 0);
            }

        }

        // 将评论 的 变动同步至最外的列表
        UpdateArticleInfoEvent updateArticleInfoEvent = new UpdateArticleInfoEvent(mArticleId);
        updateArticleInfoEvent.setReadNum(articleInfo.getReadNumber());
        EventBus.getDefault().post(updateArticleInfoEvent);

        mIsLoadOver = true;

    }

    @Override
    public void onDeleteCommentItem(View view,
                                    int position,
                                    String commentId,
                                    String topId,
                                    boolean isTop) {
        showDialog();
        mPresenter.deleteComment(position, commentId, topId, isTop);
    }

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
    public void onAudioClick(View v,
                             int adapterPosition,
                             ArticleAudioVO articleAudioVO) {
        if (articleAudioVO.getMediaInfo().getStatus() == MediaInfo.PLAY
                || articleAudioVO.getMediaInfo().getStatus() == MediaInfo.INIT) {
            jerryAudioController.pause();
        } else {
            jerryAudioController.play(articleAudioVO.getMediaInfo());
        }

    }

    @Override
    public void onSeekTo(View v,
                         int adapterPosition,
                         ArticleAudioVO articleAudioVO) {
        jerryAudioController.seekTo(articleAudioVO.getMediaInfo());
    }

    private UserAdminDialog getUserAdminDialog() {
        if (mUserAdminDialog == null) {
            mUserAdminDialog = UserAdminDialog.newInstance();
        }
        return mUserAdminDialog;
    }


    @Override
    public void onShowUserAdmin(int userId,
                                String photoUrl,
                                String userName,
                                int userLv) {
        PersonalHomePageActivity.startActivity(getContext(), userId);
    }

    @Override
    public void onShowCommentImage(ArrayList<WatchImageVO> imageList, int pos) {
        ImageWatcherActivity.startActivity(getContext(), imageList, pos);
    }

    @Override
    public void onShowDetailImage(int pos) {
        if (mImageList == null || mImageList.size() <= 0) {
            JLogUtils.showErrorQuickly("没有图片信息：" + pos);
            return;
        }
        ImageWatcherActivity.startActivity(getContext(), mImageList, pos);
    }

    @Override
    @Permission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode = 200)
    public void downloadVideo(ArticleVideoVO articleVideoVO) {
        if (DownloadUtils.isNeedShowDownloadTip()) {
            mPresenter.getDownloadVideoLimitScore(
                    mArticleId,
                    articleVideoVO.getId(),
                    articleVideoVO.getCover());
        } else {
            mPresenter.getDownloadJudgment(
                    mArticleId,
                    articleVideoVO.getId(),
                    articleVideoVO.getCover());
        }
    }

    /**
     * 弹出打赏弹窗
     */
    @Override
    public void onShowRewardTip() {
        if (mRewardArticleDialog == null) {
            mRewardArticleDialog = RewardArticleDialog.newInstance();
        }

        mRewardArticleDialog.setListener(this);
        mRewardArticleDialog.show(this);
    }

    @Override
    public void onShowRewardList() {
        RewardListActivity.startActivity(getContext(), mArticleId);
    }

    private void showDownloadVideoConfirm(int fileId,
                                          JudgementResp value,
                                          String cover) {

        String content = getContext().getString(R.string.article_detail_download_confirm_content);
        content = String.format(content, value.getLimitScore());

        new MaterialDialog.Builder(getContext())
                .title(getContext().getString(R.string.article_detail_download_confirm_title))
                .content(content)
                .checkBoxPrompt(
                        getContext().getString(R.string.article_detail_download_confirm_tip),
                        false,
                        null
                )
                .positiveColor(ContextCompat.getColor(getContext(),
                        R.color.main_blue_color))
                .positiveText(getContext().getString(R.string.common_confirm))
                .onPositive((dialog, which) -> {
                    boolean promptCheckBoxChecked = dialog.isPromptCheckBoxChecked();

                    if (promptCheckBoxChecked) {
                        DownloadUtils.uploadDate();
                    } else {
                        DownloadUtils.reset();
                    }

                    mPresenter.getDownloadJudgment(mArticleId, fileId, cover);

                })
                .negativeColor(ContextCompat.getColor(getContext(),
                        R.color.et_tip_text_color))
                .negativeText(getContext().getString(R.string.common_cancel))
                .show();

    }

    @Override
    public void onGetDownloadVideoJudgement(int fileId, JudgementResp value, String cover) {

        // 需要确认
        if (value.isIsSatisfy()) {
            showDownloadVideoConfirm(fileId, value, cover);
        } else {
            mPresenter.getDownloadJudgment(mArticleId, fileId, cover);
        }
    }

    @Override
    public void onGetDownloadJudgment(ArticleDownloadVideoResp value, String cover) {
        // 不需要扣除积分，直接下载
        if (value.isSatisfy()) {

            JerryDownload.getInstance()
                    .download(value.getVideoUrl(),
                            value.getFilename(),
                            UserCache.getDefault().getAccountId(),
                            NetCache.getDefault().getDomain(),
                            cover
                    );

            hideDialog();

//            if (download != null) {
                EventBus.getDefault().post(new DownloadAddEvent());
                ToastUtils.show(getString(R.string.cloud_download_task_add_suc));
//            } else {
//                ToastUtils.show(getString(R.string.cloud_download_task_add_failure));
//            }
            return;
        }

        hideDialog();

        RankTipDialog
                .newInstance(value.getLimitScore(), Constant.JudgementType.DOWNLOAD_VIDEO)
                .show(this);

    }

    @Override
    public void onGradeNotEnough(int limitGrade) {
        RankTipActivity.startActivity(getContext(), limitGrade, Constant.JudgementType.GRADE);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onGradeToRankNotEnough(int limitScore) {
        RankTipActivity.startActivity(getContext(), limitScore, Constant.JudgementType.GRADE_TO_RANK);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    /**
     * 打赏文章成功后的回调
     *
     * @param value
     */
    @Override
    public void onPostRewardArticle(RewardArticleResp value) {
        mRewardArticleDialog.dismiss();
        if (value.getCoinNum() < value.getRewardNum()) {
            showRewardDetailDialog(value.getCoinNum(), value.getRewardNum());
            return;
        }

        updateRewardList(UserCache.getDefault().getHeadUrl(),
                UserCache.getDefault().getAccountId(),
                value.getRewardNum());
        ToastUtils.show(getString(R.string.article_detail_reward_success));

    }

    private void showRewardDetailDialog(int coin, int reward) {
        if (mRewardArticleDetailDialog == null) {
            mRewardArticleDetailDialog = RewardArticleDetailDialog.newInstance();
        }

        if (mRewardArticleDetailDialog.isShowing()) {
            return;
        }

        mRewardArticleDetailDialog.setCoinAndReward(coin, reward);
        mRewardArticleDetailDialog.setListener(this);
        mRewardArticleDetailDialog.show(this);
    }

    @PermissionCanceled(requestCode = 200)
    private void cancel(CancelInfo cancelInfo) {
        ToastUtils.show(getString(R.string.cloud_download_need_permission));
    }

    @PermissionDenied(requestCode = 200)
    private void deny(DenyInfo denyInfo) {
        ToastUtils.show(getString(R.string.cloud_download_need_permission));
        JPermissionUtil.goToMenu(getContext());
    }

    @Override
    public void onDeleteComment(int position,
                                String commentId,
                                String topId,
                                boolean isTop,
                                boolean isShowTip) {
        if (isShowTip) {
            hideDialog();

            ToastUtils.show(getString(R.string.article_detail_delete_suc));
        }

        // TODO: 2019/5/24
//        int realPos = IS_NEED_REFRESH ? position - 1 : position;
        int realPos = position;

        EventBus.getDefault().post(new MyReviewChangeEvent(commentId));

        // 不是一级评论，直接移除即可
        if (!isTop) {
            mData.remove(position);
            mBaseAdapter.notifyItemRemoved(realPos);
            updateReviewCount(-1);
            return;
        }

        // 防止越界
        if (position >= mData.size() - 1) {
            mData.remove(position);
            mBaseAdapter.notifyItemRemoved(realPos);
            updateReviewCount(-1);
            return;
        }

        int removeCount = 1;
        int isShouldIgnoreCount = 0;

        for (int i = position + 1; i < mData.size(); ++i) {

            ArticleDetailVO articleDetailVO = mData.get(i);
            // 紧接着是 二级标题、更多、分割线的话，并且其归属id为TopId，则进行偏移endPos，否则立马终止
            if (articleDetailVO.getType() == ArticleConstants.REVIEW_SUB_ITEM) {
                ArticleReviewResp.ArticleCommentsBean.ReplyBean subItem =
                        (ArticleReviewResp.ArticleCommentsBean.ReplyBean) articleDetailVO.getData();

                if (subItem.getParentId().equals(topId)) {
                    ++removeCount;
                    continue;
                }

            } else if (articleDetailVO.getType() == ArticleConstants.REVIEW_MORE_ITEM) {

                ArticleMoreVO articleMoreVO = (ArticleMoreVO) articleDetailVO.getData();

                if (articleMoreVO.getTopReviewId().equals(topId)) {
                    ++removeCount;
                    ++isShouldIgnoreCount;
                    continue;
                }

            } else if (articleDetailVO.getType() == ArticleConstants.REVIEW_DIVIDER_ITEM) {

                ArticleDividerVO articleDividerVO = (ArticleDividerVO) articleDetailVO.getData();

                if (articleDividerVO.getTopReviewId().equals(topId)) {
                    ++removeCount;
                    ++isShouldIgnoreCount;
                    continue;
                }

            }

            break;

        }

        for (int i = removeCount - 1; i >= 0; --i) {
            mData.remove(position + i);
        }

        mBaseAdapter.notifyItemRangeRemoved(realPos, removeCount);
        int reviewCount = -(removeCount - isShouldIgnoreCount);
        updateReviewCount(reviewCount);


    }

    @Override
    public void onLoadReviewMoreError() {
        mBaseAdapter.setLoadError();
    }

    @Override
    public void onLoadReviewMore(List<ArticleDetailVO> value,
                                 int topReviewSize,
                                 long startTime) {

        mStartTime = startTime;

        mData.addAll(value);

        if (topReviewSize < LOAD_SIZE) {
            mBaseAdapter.setNoMore();
        } else {
            mBaseAdapter.setLoadComplete();
        }

    }

    @Override
    public void onArticleRemoved(List<RecommendArticleResp.ArticlesInfoBean> mRecommendList) {
    }

    @Override
    public void onDeleteArticle(String articleId) {
        hideDialog();

        ToastUtils.show(getString(R.string.edit_delete_suc));

        // 通知文章被删除
        EventBus.getDefault().post(new DeleteArticleEvent(mArticleId));

        if (getActivity() != null) {
            getActivity().finish();
        }

    }

    @Override
    public void onRankNotEnough(int limitScore) {
        RankTipActivity.startActivity(getContext(), limitScore, Constant.JudgementType.READ);
        if (getActivity() != null) {
            getActivity().finish();
        }
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

    @Override
    public void onArticleNeedValidate() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        // 进行显示验证码流程
        if (activity instanceof ArticleDetailActivity) {
            ArticleDetailActivity articleDetailActivity = (ArticleDetailActivity) activity;
            articleDetailActivity.showCode();
        }
    }

    @Override
    public void onGetArticlePermission(PermissionResp value) {
        mPermissionInfo = value;

        if (getActivity() instanceof ArticleDetailActivityListener) {
            ArticleDetailActivityListener listener = (ArticleDetailActivityListener) getActivity();
            listener.onSetPermission(value);
        }
    }

    @Override
    public void onLoadImage(ArrayList<WatchImageVO> imageList) {
        mImageList.clear();
        mImageList.addAll(imageList);
    }

    /**
     * 滚动至评论
     */
    public void scrollToReview() {
        int pos = -1;

        for (int i = 0; i < mData.size(); i++) {
            ArticleDetailVO item = mData.get(i);

            if (item.getType() == ArticleConstants.REVIEW_TITLE) {
                pos = i;
                break;
            }
        }

        if (pos == -1) {
            return;
        }

        ((LinearLayoutManager) mRecycleView.getLayoutManager())
                .scrollToPositionWithOffset(pos, 0);

    }

    /**
     * 获取评论头的下标
     */
    public int getReviewTopPos() {
        int pos = -1;

        for (int i = 0; i < mData.size(); i++) {
            ArticleDetailVO item = mData.get(i);

            if (item.getType() == ArticleConstants.REVIEW_TITLE) {
                pos = i;
                break;
            }
        }

        return pos;
    }

    /**
     * 获取分享链接
     */
    public String getShareLink() {
        if (mArticleInfo == null) {
            return "";
        }

        String articleId = mArticleInfo.getArticleId();

        return NetConfig.getArticleUrlPrefix() + articleId;
    }

    public String getArticleTitle() {
        if (mArticleInfo == null) {
            return "";
        }
        return mArticleInfo.getTitle();
    }

    public int getAuthorId() {
        if (mArticleInfo == null) {
            return -1;
        }
        return mArticleInfo.getAccountId();
    }

    @Override
    protected int getInitRegister() {
        return EVENT_BUS | BUTTER_KNIFE;
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

        int resultPos = event.getPos() + 1;

        // 只有是一级的时候，才有可能存在 "评论为空" 的占位图;
        // 找到 "评论为空" 的item，删除并更新
        if (event.getArticleDetailVO().getType() == ArticleConstants.REVIEW_TOP_ITEM) {
            int emptyIndex = -1;

            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getType() == ArticleConstants.REVIEW_EMPTY) {
                    emptyIndex = i;
                    break;
                }
            }

            if (emptyIndex != -1) {
                mData.remove(emptyIndex);
                mBaseAdapter.notifyItemRemoved(emptyIndex);
            }
        }

        mData.add(resultPos, event.getArticleDetailVO());

        mBaseAdapter.notifyItemInserted(resultPos);

        updateReviewCount(1);

    }

    /**
     * 刷新全部评论的条数
     *
     * @param updateCount 更新的条数，正数为增加，负数为减少
     */
    private void updateReviewCount(int updateCount) {

        int reviewTitleIndex = -1;

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getType() == ArticleConstants.REVIEW_TITLE) {
                reviewTitleIndex = i;
                break;
            }
        }

        if (reviewTitleIndex == -1) {
            return;
        }

        TitleCountVO titleCountVO =
                (TitleCountVO) mData.get(reviewTitleIndex).getData();

        int count = titleCountVO.getCount() + updateCount;

        if (count < 0) {
            count = 0;
        }

        titleCountVO.setCount(count);

        mBaseAdapter.notifyItemChanged(reviewTitleIndex);

        // 评论数为0时，添加一个 "评论为空"
        // 评论头的下一项增加 "评论为空"
        if (count == 0) {
            mData.add(reviewTitleIndex + 1,
                    new ArticleDetailVO<>(ArticleConstants.REVIEW_EMPTY, new Object()));
        }

        if (getActivity() instanceof ArticleDetailActivityListener) {
            ArticleDetailActivityListener listener = (ArticleDetailActivityListener) getActivity();
            listener.onSetCount(count);
        }

        // 将评论 的 变动同步至最外的列表
        UpdateArticleInfoEvent updateArticleInfoEvent = new UpdateArticleInfoEvent(mArticleId);
        updateArticleInfoEvent.setCommentNum(count);
        EventBus.getDefault().post(updateArticleInfoEvent);

    }

    /**
     * 同步 评论详情 的 添加评论
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncAddReview(DetailReviewAddSyncEvent event) {

        int topIndex = -1;

        for (int i = 0; i < mData.size(); i++) {

            ArticleDetailVO articleDetailVO = mData.get(i);

            // 不是一级评论，直接跳过
            if (articleDetailVO.getType() != ArticleConstants.REVIEW_TOP_ITEM) {
                continue;
            }

            ArticleReviewResp.ArticleCommentsBean topItem =
                    (ArticleReviewResp.ArticleCommentsBean) articleDetailVO.getData();

            if (topItem.getId().equals(event.getParentId())) {
                topIndex = i;
                break;
            }

        }

        // 添加子评论
        int subIndex = topIndex + 1;
        ArticleDetailVO subItem =
                new ArticleDetailVO<>(ArticleConstants.REVIEW_SUB_ITEM, event.getReviewData());
        mData.add(subIndex, subItem);
        mBaseAdapter.notifyItemInserted(subIndex);

        boolean isNeedAddDivider = false;
        if (subIndex + 1 > mData.size()) {
            isNeedAddDivider = true;
        } else if (mData.get(subIndex + 1).getType() != ArticleConstants.REVIEW_SUB_ITEM
                && mData.get(subIndex + 1).getType() != ArticleConstants.REVIEW_MORE_ITEM
                && mData.get(subIndex + 1).getType() != ArticleConstants.REVIEW_SUB_EMPTY) {
            isNeedAddDivider = true;
        }

        if (isNeedAddDivider) {
            // 添加分割线
            mData.add(subIndex + 1, new ArticleDetailVO<>(ArticleConstants.REVIEW_DIVIDER_ITEM,
                    new ArticleDividerVO(event.getReviewData().getArticleId(),
                            event.getReviewData().getParentId())));
            mBaseAdapter.notifyItemInserted(subIndex + 1);
        }

        updateReviewCount(1);

    }

    /**
     * 打赏成功后更新打赏列表
     *
     * @param url    头像地址
     * @param userId 用户id
     * @param coin   打赏金币
     */
    private void updateRewardList(String url, int userId, int coin) {
        int position = -1;

        for (int i = 0; i < mData.size(); i++) {
            ArticleDetailVO articleDetailVO = mData.get(i);

            if (articleDetailVO.getType() == ArticleConstants.ARTICLE_REWARD) {

                RewardListResp rewardList = (RewardListResp) articleDetailVO.getData();
                rewardList.setTotalNum(rewardList.getTotalNum() + 1);

                RewardListResp.RewardListBean item = new RewardListResp.RewardListBean();
                item.setHeadUrl(url);
                item.setRewardId(userId);
                item.setAmount(coin);

                if (rewardList.getRewardListBeanList() == null) {
                    rewardList.setRewardListBeanList(new ArrayList<RewardListResp.RewardListBean>());
                }

                rewardList.getRewardListBeanList().add(0, item);

                position = i;
                break;
            }

        }

        if (position == -1) {
            return;
        }

        mBaseAdapter.notifyItemChanged(position);
    }

    /**
     * 同步 评论详情 的 删除评论
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncDelReview(DetailReviewDelSyncEvent event) {

        int position = -1;
        boolean isTop = true;

        for (int i = 0; i < mData.size(); i++) {
            ArticleDetailVO articleDetailVO = mData.get(i);

            if (articleDetailVO.getType() != ArticleConstants.REVIEW_TOP_ITEM
                    && articleDetailVO.getType() != ArticleConstants.REVIEW_SUB_ITEM) {
                continue;
            }

            String commentId = null;
            if (articleDetailVO.getType() == ArticleConstants.REVIEW_TOP_ITEM) {
                ArticleReviewResp.ArticleCommentsBean topItem
                        = (ArticleReviewResp.ArticleCommentsBean) articleDetailVO.getData();

                commentId = topItem.getId();
                isTop = true;

            } else if (articleDetailVO.getType() == ArticleConstants.REVIEW_SUB_ITEM) {
                ArticleReviewResp.ArticleCommentsBean.ReplyBean subItem
                        = (ArticleReviewResp.ArticleCommentsBean.ReplyBean) articleDetailVO.getData();

                commentId = subItem.getId();
                isTop = false;
            }

            if (commentId != null && commentId.equals(event.getCommentId())) {
                position = i;
                break;
            }

        }

        if (position == -1) {
            return;
        }

        onDeleteComment(position,
                event.getCommentId(),
                event.getParentId(),
                isTop,
                false);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginState(LoginStateChangeEvent loginStateChangeEvent) {
        mBaseAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getEmptyView() {
        return R.layout.blank_white_no_exist_article;
    }

    @Override
    protected int getEmptyBtnId() {
        return BitManager.NO_CLICK;
    }

    /**
     * 删除文章
     */
    public void deleteArticle() {
        if (getContext() == null) {
            return;
        }
        new MaterialDialog
                .Builder(getContext())
                .title(R.string.edit_delete_confirm)
                .content(String.format(getString(R.string.edit_delete_title),
                        mArticleInfo.getTitle()))
                .positiveText(R.string.common_cancel)
                .negativeText(R.string.common_confirm)
                .positiveColor(ContextCompat.getColor(getContext(), R.color.main_blue_color))
                .negativeColor(ContextCompat.getColor(getContext(), R.color.et_tip_text_color))
                .onNegative((dialog, which) -> {
                    showDialog();
                    mPresenter.deleteArticle(mArticleInfo.getArticleId());
                })
                .show();
    }

    public void modifyArticle() {
        EditArticleActivity.startActivity(getContext(), HomePageFragment.mCategoryData, mArticleId);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (jerryAudioController != null) {
            jerryAudioController.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (jerryAudioController != null) {
            jerryAudioController.release();
        }
        articleDetailAdapter.getWebViewCache().destroyWebView();
    }

    public boolean isLoadOver() {
        return mIsLoadOver;
    }

    public String getAccount() {
        return mArticleInfo.getAccount();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void articleHideStatusChange(HideArticleEvent event) {
        mArticleInfo.setStatus(event.getStatu());
        mBaseAdapter.notifyDataSetChanged();
    }

    /**
     * 从打赏文章弹窗传出来的接口方法
     *
     * @param num
     */
    @Override
    public void onPostArticleReward(int num) {
        mPresenter.postRewardArticle(mArticleId, num);
    }

    @Override
    public void onShowArticleRewardNotEnough() {
        mRewardArticleDetailDialog.dismiss();
        onShowRewardTip();
    }

    /**
     * 返回文章置顶顺序
     *
     * @return
     */
    public int getTopPosition() {
        return mTopPosition;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTopArticlePosition(TopArticlePositionEvent event) {
        mTopPosition = event.getPosition();
    }
}
