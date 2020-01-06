package com.tysq.ty_android.feature.articleDetail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.utils.UIUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.bit.view.fragment.BitLazyFragment;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.jerry.image_watcher.ImageWatcherActivity;
import com.jerry.image_watcher.model.WatchImageVO;
import com.jerry.media.controller.JerryAudioController;
import com.jerry.media.listener.JerryStatusListener;
import com.jerry.media.model.MediaInfo;
import com.jerry.media.widget.TimeBar;
import com.luck.picture.lib.tools.ScreenUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.AdvertisementViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.ArticleDeleteViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.ArticleRewardViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.AudioContentViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.BottomViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.ImageContentViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.RecommendContentViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.RecommendTitleViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.ReviewEmptyViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.ReviewTitleViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.ReviewTopViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.SubReviewDividerViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.SubReviewMoreViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.SubReviewViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.TextContentViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.TitleViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.VideoContentViewHolder;
import com.tysq.ty_android.feature.articleDetail.config.ArticleConstants;
import com.tysq.ty_android.feature.articleDetail.listener.ArticleDetailListener;
import com.tysq.ty_android.feature.personalHomePage.PersonalHomePageActivity;
import com.tysq.ty_android.feature.reviewDetail.activity.ReviewDetailActivity;
import com.tysq.ty_android.feature.video.VideoActivity;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.local.sp.WebViewCache;
import com.tysq.ty_android.utils.TyUtils;
import com.tysq.ty_android.widget.TagFlowLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import jerrEditor.parser.Html;
import response.LabelResp;
import response.article.ArticleDetailResp;
import response.article.ArticleReviewResp;
import response.article.RecommendArticleResp;
import response.article.RewardListResp;
import response.common.TitleCountVO;
import vo.article.ArticleAudioVO;
import vo.article.ArticleDetailVO;
import vo.article.ArticleImageVO;
import vo.article.ArticleMoreVO;
import vo.article.ArticleTextVO;
import vo.article.ArticleVideoVO;

/**
 * author       : frog
 * time         : 2019/5/20 上午9:25
 * desc         : 文章详情
 * version      : 1.3.0
 */
public class ArticleDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final WeakReference<Context> mContext;
    private final WeakReference<BitBaseFragment> mFragment;
    private final LayoutInflater mInflater;
    private final List<ArticleDetailVO> mData;
    private List<RewardListResp.RewardListBean> rewardListBeanList;
    private WebViewCache mWebView;

    private static final int NANO = 1000_000_000;

    private final float mImgPadding;

    private ArticleDetailListener mListener;

    public ArticleDetailAdapter(BitLazyFragment mFragment,
                                Context context,
                                List<ArticleDetailVO> data) {
        this.mFragment = new WeakReference<>(mFragment);
        this.mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mImgPadding = UIUtils.dip2px(context, 20);
        this.mWebView = new WebViewCache(context,
                Constant.HtmlUrl.DETAIL_AD,
                Constant.AdvertisementNum.DETAIL_AD);
    }

    public void setListener(ArticleDetailListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == ArticleConstants.TITLE) {
            viewHolder = new TitleViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_title, parent, false));

        } else if (viewType == ArticleConstants.CONTENT_TEXT) {
            viewHolder = new TextContentViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_text_content, parent, false));

        } else if (viewType == ArticleConstants.CONTENT_IMAGE) {
            viewHolder = new ImageContentViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_image, parent, false));

        } else if (viewType == ArticleConstants.CONTENT_AUDIO) {
            viewHolder = new AudioContentViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_audio, parent, false));

        } else if (viewType == ArticleConstants.CONTENT_VIDEO) {
            viewHolder = new VideoContentViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_video, parent, false));

        } else if (viewType == ArticleConstants.RECOMMEND_TITLE) {
            viewHolder = new RecommendTitleViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_recommend_title, parent, false));

        } else if (viewType == ArticleConstants.ADVERTISEMENT_ITEM) {
            viewHolder = new AdvertisementViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_advertisement, parent, false));

        } else if (viewType == ArticleConstants.RECOMMEND_ITEM) {
            viewHolder = new RecommendContentViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_recommend_item, parent, false));

        } else if (viewType == ArticleConstants.REVIEW_TITLE) {
            viewHolder = new ReviewTitleViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_review_title, parent, false));

        } else if (viewType == ArticleConstants.REVIEW_TOP_ITEM) {
            viewHolder = new ReviewTopViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_review_top, parent, false));

        } else if (viewType == ArticleConstants.REVIEW_EMPTY) {
            viewHolder = new ReviewEmptyViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_review_empty, parent, false));
        } else if (viewType == ArticleConstants.BOTTOM) {
            viewHolder = new BottomViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_bottom, parent, false));

        } else if (viewType == ArticleConstants.REVIEW_SUB_ITEM) {
            viewHolder = new SubReviewViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_review_sub, parent, false));

        } else if (viewType == ArticleConstants.REVIEW_MORE_ITEM) {
            viewHolder = new SubReviewMoreViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_review_more, parent, false));

        } else if (viewType == ArticleConstants.REVIEW_DIVIDER_ITEM) {
            viewHolder = new SubReviewDividerViewHolder(mInflater
                    .inflate(R.layout.item_article_detail_review_divider, parent, false));
        } else if (viewType == ArticleConstants.ARTICLE_EMPTY) {
            viewHolder = new ArticleDeleteViewHolder(mInflater
                    .inflate(R.layout.blank_white_no_exist_article, parent, false));
        } else if (viewType == ArticleConstants.ARTICLE_REWARD) {
            viewHolder = new ArticleRewardViewHolder(
                    mInflater.inflate(R.layout.item_article_detail_reward, parent, false));
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {

        ArticleDetailVO itemInfo = mData.get(position);

        if (holder instanceof TitleViewHolder) { // 文章信息
            ArticleDetailResp.ArticleInfoBean articleInfo
                    = (ArticleDetailResp.ArticleInfoBean) itemInfo.getData();

            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;

            titleViewHolder.tvAuthor.setText(articleInfo.getAccount());
            titleViewHolder.tvAuthor.setOnClickListener(v -> {
                PersonalHomePageActivity.startActivity(mContext.get(), articleInfo.getAccountId());
            });

            titleViewHolder.tvReading.setText(TyUtils.formatNum(articleInfo.getReadNumber()));

            String content = "";

            if (articleInfo.getParentCategoryName() == null
                    || articleInfo.getParentCategoryName().trim().length() <= 0) {
                titleViewHolder.tvColumn.setVisibility(View.GONE);
            } else {

                if (articleInfo.getCategoryName() == null || articleInfo.getCategoryName().trim().length() <= 0) {
                    content = articleInfo.getParentCategoryName();
                } else {
                    content = articleInfo.getParentCategoryName() + " · " + articleInfo.getCategoryName();
                }
            }


            titleViewHolder.tvColumn.setText(content);

            TyUtils.initUserPhoto(
                    mFragment.get(),
                    mContext.get(),
                    articleInfo.getHeadUrl(),
                    titleViewHolder.ivPhoto);

            titleViewHolder.ivPhoto.setOnClickListener(v -> {
                PersonalHomePageActivity.startActivity(mContext.get(), articleInfo.getAccountId());
            });

            titleViewHolder.tvTime.setText(TyUtils.getPublishTime(articleInfo.getCreatedTime()));

            createTag(articleInfo.getLabel(), titleViewHolder.tagFlow);

            switch (articleInfo.getStatus()) {
                case Constant.MyArticleType.PUBLISH:        // 发布
                    titleViewHolder.ivArticleState.setVisibility(View.GONE);

                    titleViewHolder.tvReason.setVisibility(View.GONE);

                    titleViewHolder.tvTitle.setText(articleInfo.getTitle());
                    break;

                case Constant.MyArticleType.DRAFT:          // 草稿
                    titleViewHolder.ivArticleState.setVisibility(View.GONE);

                    titleViewHolder.tvReason.setVisibility(View.GONE);

                    TyUtils.addDraftTag(mContext.get(),
                            titleViewHolder.tvTitle,
                            articleInfo.getTitle());
                    break;

                case Constant.MyArticleType.REFUSE:         // 驳回
                    titleViewHolder.ivArticleState.setVisibility(View.VISIBLE);
                    titleViewHolder.ivArticleState.setImageResource(R.drawable.ic_refuse);

                    titleViewHolder.tvReason.setVisibility(View.VISIBLE);
                    titleViewHolder.tvReason.setText(
                            String.format(mContext.get().getString(R.string.my_article_refuse_reason),
                                    articleInfo.getReason())
                    );

                    titleViewHolder.tvTitle.setText(articleInfo.getTitle());
                    break;

                case Constant.MyArticleType.EXAM:           // 审核
                    titleViewHolder.ivArticleState.setVisibility(View.VISIBLE);
                    titleViewHolder.ivArticleState.setImageResource(R.drawable.ic_wait_examination);

                    titleViewHolder.tvReason.setVisibility(View.GONE);

                    titleViewHolder.tvTitle.setText(articleInfo.getTitle());
                    break;

                case Constant.MyArticleType.HIDE:
                    titleViewHolder.ivArticleState.setVisibility(View.GONE);

                    titleViewHolder.tvReason.setVisibility(View.GONE);

                    titleViewHolder.tvTitle.setText(articleInfo.getTitle());
                    break;
            }

        } else if (holder instanceof TextContentViewHolder) { // 文本信息

            ArticleTextVO articleTextVO = (ArticleTextVO) itemInfo.getData();

            TextContentViewHolder textContentViewHolder = (TextContentViewHolder) holder;
            textContentViewHolder.tvContent.setLineSpacing(UIUtils.dip2px(mContext.get(), 6), 1);

            textContentViewHolder.tvContent.setText(
                    Html.fromHtml(articleTextVO.getContent(), null));

        } else if (holder instanceof VideoContentViewHolder) { // 视频信息

            final ArticleVideoVO articleVideoVO = (ArticleVideoVO) itemInfo.getData();

            VideoContentViewHolder videoContentViewHolder = (VideoContentViewHolder) holder;

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.shape_gray_fill)
                    .error(R.drawable.shape_gray_fill);

            String imageUrl = articleVideoVO.getCover();

            TyUtils.getGlideRequest(mFragment.get(),
                    mContext.get(),
                    imageUrl,
                    requestOptions,
                    videoContentViewHolder.ivCover);

            videoContentViewHolder.ivImageWatcher.setOnClickListener(v -> {
                ImageWatcherActivity.startActivity(mContext.get(),
                        new WatchImageVO(imageUrl, articleVideoVO.getOriginalUrl())
                );
            });

            videoContentViewHolder.ivDownload.setOnClickListener(v -> {
                if (mListener == null) {
                    return;
                }

                mListener.downloadVideo(articleVideoVO);
            });

            videoContentViewHolder.rlVideo.setOnClickListener(v -> {
                if (articleVideoVO.getStatus() == Constant.VideoStatus.ENCODE_OVER
                        || articleVideoVO.getStatus() == Constant.VideoStatus.EXAM_MP4) {
                    VideoActivity.startActivity(mContext.get(),
                            articleVideoVO.getUrl(),
                            articleVideoVO.getTitle(),
                            articleVideoVO.getOriginalUrl());
                } else {
                    ToastUtils.show(mContext.get().getString(R.string.article_detail_video_encoding));
                }

            });

        } else if (holder instanceof AudioContentViewHolder) { // 音频信息

            AudioContentViewHolder audioViewHolder = (AudioContentViewHolder) holder;

            final ArticleAudioVO articleAudioVO = (ArticleAudioVO) itemInfo.getData();
            // 设置名称
            audioViewHolder.tvAudioName.setText(articleAudioVO.getAudioName());

            final MediaInfo mediaInfo = articleAudioVO.getMediaInfo();

            setAudioStatus(mediaInfo, audioViewHolder);

            audioViewHolder.ivControlBtn.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onAudioClick(audioViewHolder.itemView,
                            audioViewHolder.getAdapterPosition(),
                            articleAudioVO);
                }
            });

            audioViewHolder.sbProcess.removeAllListeners();

            audioViewHolder.sbProcess.addListener(new TimeBar.OnScrubListener() {
                @Override
                public void onScrubStart(TimeBar timeBar, long position) {
                    mediaInfo.setMoveBar(true);
                    articleAudioVO.getMediaInfo().setCurPos(position);
                    setTime(mediaInfo, audioViewHolder);
                }

                @Override
                public void onScrubMove(TimeBar timeBar, long position) {
                    articleAudioVO.getMediaInfo().setCurPos(position);
                    setTime(mediaInfo, audioViewHolder);
                }

                @Override
                public void onScrubStop(TimeBar timeBar,
                                        long position,
                                        boolean canceled) {
                    mListener.onSeekTo(audioViewHolder.itemView,
                            audioViewHolder.getAdapterPosition(),
                            articleAudioVO);
                    mediaInfo.setMoveBar(false);
                }
            });


        } else if (holder instanceof ImageContentViewHolder) { // 图片信息

            ArticleImageVO itemInfoData = (ArticleImageVO) itemInfo.getData();

            ImageContentViewHolder imageContentViewHolder = (ImageContentViewHolder) holder;

            final ImageView ivImg = imageContentViewHolder.ivImg;

            ViewGroup.LayoutParams layoutParams = ivImg.getLayoutParams();
            layoutParams.width = (int) TyUtils.getWidth(itemInfoData.getWidth(), mImgPadding);
            layoutParams.height = (int) TyUtils.getHeight(
                    itemInfoData.getWidth(),
                    itemInfoData.getHeight(),
                    layoutParams.width);
            ivImg.setLayoutParams(layoutParams);

            RequestOptions requestOptions = new RequestOptions()
                    .error(R.drawable.placeholder_error)
                    .placeholder(R.drawable.placeholder_loading);

            TyUtils.getGlideRequest(mFragment.get(),
                    mContext.get(),
                    itemInfoData.getUrl(),
                    requestOptions,
                    ivImg);

            imageContentViewHolder.ivImg.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onShowDetailImage(itemInfoData.getPos());
                }
            });

        } else if (holder instanceof ArticleRewardViewHolder) {  //打赏列表
            RewardListResp rewardArticleResp = (RewardListResp) itemInfo.getData();
            rewardListBeanList = rewardArticleResp.getRewardListBeanList();
            ArticleRewardViewHolder articleRewardViewHolder = (ArticleRewardViewHolder) holder;

            if (rewardArticleResp.getTotalNum() <= 0) {
                articleRewardViewHolder.rlRewardBtn.setVisibility(View.GONE);
            } else {
                articleRewardViewHolder.rlRewardBtn.setVisibility(View.VISIBLE);
            }
            String format = String.format(
                    mContext.get().getString(R.string.article_detail_reward_people_num),
                    rewardArticleResp.getTotalNum()
            );
            articleRewardViewHolder.tvRewardNum.setText(format);

            createRewardTag(rewardListBeanList,
                    articleRewardViewHolder.tagFlowLayout,
                    rewardArticleResp.getTotalNum(),
                    articleRewardViewHolder.ivMore,
                    articleRewardViewHolder.rlRewardList);

            articleRewardViewHolder.tvArticleReward.setOnClickListener(v -> mListener.onShowRewardTip());

            articleRewardViewHolder.ivMore.setOnClickListener(v -> mListener.onShowRewardList());

        } else if (holder instanceof RecommendTitleViewHolder) { // 相关推荐 头。

        } else if (holder instanceof RecommendContentViewHolder) { // 相关推荐 内。
            RecommendArticleResp.ArticlesInfoBean recommendItem
                    = (RecommendArticleResp.ArticlesInfoBean) itemInfo.getData();

            RecommendContentViewHolder recommendContentViewHolder
                    = (RecommendContentViewHolder) holder;
            recommendContentViewHolder.tvTitle.setText(recommendItem.getTitle());
            recommendContentViewHolder.tvTime
                    .setText(TyUtils.getPublishTime(recommendItem.getCreatedTime()));
            recommendContentViewHolder.tvAuthor.setText(recommendItem.getAccount());
            recommendContentViewHolder.tvReadCount
                    .setText(TyUtils.formatNum(recommendItem.getReadNumber()));
            recommendContentViewHolder.tvReviewCount
                    .setText(TyUtils.formatNum(recommendItem.getCommentNumber()));
            recommendContentViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArticleDetailActivity.startActivity(mContext.get(),
                            recommendItem.getArticleId());
                }
            });

        } else if (holder instanceof AdvertisementViewHolder) { //广告位

        } else if (holder instanceof ReviewTitleViewHolder) { // 评论头
            TitleCountVO titleCountVO = (TitleCountVO) itemInfo.getData();

            ReviewTitleViewHolder reviewTitleViewHolder = (ReviewTitleViewHolder) holder;

            String count = TyUtils.formatNum(titleCountVO.getCount());
            count = TextUtils.isEmpty(count) ? "0" : count;

            reviewTitleViewHolder
                    .tvTitle
                    .setText(String.format(mContext.get().getString(R.string.article_detail_review),
                            count));

        } else if (holder instanceof ReviewTopViewHolder) { // 评论——一级

            ArticleReviewResp.ArticleCommentsBean reviewItem
                    = (ArticleReviewResp.ArticleCommentsBean) itemInfo.getData();

            ReviewTopViewHolder reviewTopViewHolder = (ReviewTopViewHolder) holder;

            // 评论内容
            String content = reviewItem.getContent();
            if (TextUtils.isEmpty(content)) {
                reviewTopViewHolder.tvContent.setVisibility(View.GONE);
            } else {
                reviewTopViewHolder.tvContent.setVisibility(View.VISIBLE);
                reviewTopViewHolder.tvContent.setText(content);
            }

            reviewTopViewHolder.tvTitle.setText(reviewItem.getCommentatorName());
            reviewTopViewHolder.tvTime.setText(
                    TyUtils.getPublishTime(reviewItem.getTime())
            );

            TyUtils.initUserPhoto(
                    mFragment.get(),
                    mContext.get(),
                    reviewItem.getIconUrl(),
                    reviewTopViewHolder.ivPhoto);

            createReviewImage(reviewTopViewHolder.tagFlowLayout, reviewItem.getImageUrl());

            boolean isHaveDeletePermission = false;
            if (reviewItem.isCanDelete()) {
                isHaveDeletePermission = true;
            } else if (!UserCache.isEmpty() &&
                    UserCache.getDefault().getAccountId() == reviewItem.getCommentatorId()) {
                isHaveDeletePermission = true;
            }

            if (isHaveDeletePermission) {
                reviewTopViewHolder.ivDelete.setVisibility(View.VISIBLE);
                // 删除评论
                reviewTopViewHolder.ivDelete.setOnClickListener(v -> {
                    new MaterialDialog.Builder(mContext.get())
                            .content(mContext.get().getString(R.string.article_detail_delete_confirm))
                            .negativeColor(ContextCompat.getColor(mContext.get(),
                                    R.color.et_tip_text_color))
                            .negativeText(mContext.get().getString(R.string.common_confirm))
                            .onNegative((dialog, which) -> {
                                if (mListener == null) {
                                    return;
                                }

                                mListener.onDeleteCommentItem(v, holder.getAdapterPosition(),
                                        reviewItem.getId(), reviewItem.getId(), true);
                            })
                            .positiveColor(ContextCompat.getColor(mContext.get(),
                                    R.color.main_blue_color))
                            .positiveText(mContext.get().getString(R.string.common_cancel))
                            .show();
                });
            } else {
                reviewTopViewHolder.ivDelete.setVisibility(View.GONE);
            }

            reviewTopViewHolder.ivReview.setOnClickListener(v -> {
                mListener.onReplyComment(v,
                        holder.getAdapterPosition(),         // 位置
                        reviewItem.getArticleId(),           // 文章id
                        reviewItem.getCommentatorId(),       // 评论者id
                        reviewItem.getCommentatorName(),     // 评论者的名字
                        reviewItem.getId(),                  // 该条评论的id
                        reviewItem.getId());                 // 一级评论的id
            });

            // 弹权限
            reviewTopViewHolder.ivPhoto.setOnClickListener(v -> {
                mListener.onShowUserAdmin(
                        reviewItem.getCommentatorId(),
                        reviewItem.getIconUrl(),
                        reviewItem.getCommentatorName(),
                        1
                );
            });

            // 弹权限
            reviewTopViewHolder.tvTitle.setOnClickListener(v -> {
                mListener.onShowUserAdmin(
                        reviewItem.getCommentatorId(),
                        reviewItem.getIconUrl(),
                        reviewItem.getCommentatorName(),
                        1
                );
            });

        } else if (holder instanceof SubReviewViewHolder) { // 二级评论
            SubReviewViewHolder subReviewViewHolder = (SubReviewViewHolder) holder;

            ArticleReviewResp.ArticleCommentsBean.ReplyBean replyBean
                    = (ArticleReviewResp.ArticleCommentsBean.ReplyBean) mData.get(position).getData();

            createReviewImage(subReviewViewHolder.tagFlowLayout, replyBean.getImageUrl());

            boolean isHaveDeletePermission = false;
            if (replyBean.isCanDelete()) {
                isHaveDeletePermission = true;
            } else if (!UserCache.isEmpty() &&
                    UserCache.getDefault().getAccountId() == replyBean.getCommentatorId()) {
                isHaveDeletePermission = true;
            }

            if (isHaveDeletePermission) {
                subReviewViewHolder.ivDelete.setVisibility(View.VISIBLE);
                // 删除评论
                subReviewViewHolder.ivDelete.setOnClickListener(v -> {
                    new MaterialDialog.Builder(mContext.get())
                            .content(mContext.get().getString(R.string.article_detail_delete_confirm))
                            .negativeColor(ContextCompat.getColor(mContext.get(),
                                    R.color.et_tip_text_color))
                            .negativeText(mContext.get().getString(R.string.common_confirm))
                            .onNegative((dialog, which) -> {
                                if (mListener == null) {
                                    return;
                                }

                                mListener.onDeleteCommentItem(v, holder.getAdapterPosition(),
                                        replyBean.getId(), replyBean.getParentId(), false);
                            })
                            .positiveColor(ContextCompat.getColor(mContext.get(),
                                    R.color.main_blue_color))
                            .positiveText(mContext.get().getString(R.string.common_cancel))
                            .show();
                });
            } else {
                subReviewViewHolder.ivDelete.setVisibility(View.GONE);
            }

            subReviewViewHolder.ivReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onReplyComment(v,
                            holder.getAdapterPosition(),        // 位置
                            replyBean.getArticleId(),           // 文章id
                            replyBean.getCommentatorId(),       // 评论者id
                            replyBean.getCommentatorName(),     // 评论者的名字
                            replyBean.getId(),                  // 该条评论的id
                            replyBean.getParentId());           // 一级评论的id
                }
            });

            // 弹 评论人的 权限
            subReviewViewHolder.tvSenderName.setOnClickListener(v -> {
                mListener.onShowUserAdmin(
                        replyBean.getCommentatorId(),
                        replyBean.getIconUrl(),
                        replyBean.getCommentatorName(),
                        replyBean.getCommentatorGrade()
                );
            });

            // 弹 被评论人的 权限
            subReviewViewHolder.tvReceiverName.setOnClickListener(v -> {
                mListener.onShowUserAdmin(
                        replyBean.getCommentedId(),
                        replyBean.getCommentedIcon(),
                        replyBean.getCommentedName(),
                        replyBean.getCommentedGrade()
                );
            });

            // 弹 评论人的 权限
            subReviewViewHolder.ivPhoto.setOnClickListener(v -> {
                mListener.onShowUserAdmin(
                        replyBean.getCommentatorId(),
                        replyBean.getIconUrl(),
                        replyBean.getCommentatorName(),
                        replyBean.getCommentatorGrade()
                );
            });

            // 评论内容
            String content = replyBean.getContent();
            if (TextUtils.isEmpty(content)) {
                subReviewViewHolder.tvContent.setVisibility(View.GONE);
            } else {
                subReviewViewHolder.tvContent.setVisibility(View.VISIBLE);
                subReviewViewHolder.tvContent.setText(content);
            }

            subReviewViewHolder.tvTime.setText(
                    TyUtils.getPublishTime(replyBean.getTime() / NANO)
            );

            // 添加图片
            TyUtils.initUserPhoto(
                    mFragment.get(),
                    mContext.get(),
                    replyBean.getIconUrl(),
                    subReviewViewHolder.ivPhoto);

            subReviewViewHolder.tvSenderName.setText(replyBean.getCommentatorName());
            // 处理是否要显示接受者名字
            if (replyBean.getParentId().equals(replyBean.getFatherId())) {
                subReviewViewHolder.ivArrow.setVisibility(View.GONE);
                subReviewViewHolder.tvReceiverName.setVisibility(View.GONE);
            } else {
                subReviewViewHolder.ivArrow.setVisibility(View.VISIBLE);
                subReviewViewHolder.tvReceiverName.setVisibility(View.VISIBLE);
                subReviewViewHolder.tvReceiverName.setText(replyBean.getCommentedName());
            }

            // 处理分割线
            if (position + 1 < mData.size()
                    && (mData.get(position + 1).getType() == ArticleConstants.REVIEW_SUB_ITEM
                    || mData.get(position + 1).getType() == ArticleConstants.REVIEW_MORE_ITEM)) {
                subReviewViewHolder.vDivider.setVisibility(View.VISIBLE);
            } else {
                subReviewViewHolder.vDivider.setVisibility(View.GONE);
            }

        } else if (holder instanceof SubReviewMoreViewHolder) {
            SubReviewMoreViewHolder moreViewHolder = (SubReviewMoreViewHolder) holder;

            ArticleMoreVO articleMoreVO = (ArticleMoreVO) mData.get(position).getData();

            moreViewHolder.tvMore.setOnClickListener(v -> {
                Log.i("more", "onBindViewHolder: " + articleMoreVO.toString());
                ReviewDetailActivity.startActivity(mContext.get(),
                        articleMoreVO.getArticleId(),
                        articleMoreVO.getTopReviewId(),
                        articleMoreVO.isCanDelete(),
                        articleMoreVO.isCanForbid());
            });

        }

    }

    /**
     * 创建打赏标签
     */
    private void createRewardTag(List<RewardListResp.RewardListBean> dataList,
                                 TagFlowLayout tagFlowLayout,
                                 int count,
                                 ImageView imageView,
                                 LinearLayout relativeLayout){

        tagFlowLayout.removeAllViews();

        if (dataList == null || dataList.size() <= 0){
            relativeLayout.setVisibility(View.GONE);
            return;
        }

        int tagCount = 0;
        int width = TyUtils.getAverageWidth(mContext.get(), Constant.REWARD_ITEM_NUM);
        relativeLayout.setVisibility(View.VISIBLE);
        for (RewardListResp.RewardListBean rewardListBean : dataList){
            View view = mInflater.inflate(R.layout.widget_article_detail_reward,
                    tagFlowLayout,
                    false);
            ImageView ivAvatar = view.findViewById(R.id.iv_avatar);
            TextView tvRewardNum = view.findViewById(R.id.tv_reward_num);

            RequestOptions requestOptions = RequestOptions
                    .bitmapTransform(new CircleCrop())
                    .error(R.drawable.placeholder_user_photo)
                    .placeholder(R.drawable.placeholder_user_photo);

            TyUtils.getGlideRequest(
                    mFragment.get(),
                    mContext.get(),
                    rewardListBean.getHeadUrl(),
                    requestOptions,
                    ivAvatar
            );
            ivAvatar.setOnClickListener(view1 ->
                    PersonalHomePageActivity.startActivity(mContext.get(), rewardListBean.getRewardId()
                    ));

            String format = String.format(
                    mContext.get().getString(R.string.article_detail_reward_num),
                    rewardListBean.getAmount()
            );

            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = width;

            tagCount += width;

            tvRewardNum.setText(format);
            tagFlowLayout.addView(view);
        }
        tagFlowLayout.setNeedShowLine(1);

        if (tagCount > ScreenUtils.getScreenWidth(mContext.get())){
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = width;
            imageView.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.GONE);
        }

    }

    /**
     * 填充用户图片
     */
    private void createReviewImage(TagFlowLayout tagFlowLayout,
                                   final List<ArticleReviewResp.ImageUrlBean> imageUrlList) {

        tagFlowLayout.removeAllViews();

        if (imageUrlList == null) {
            tagFlowLayout.setVisibility(View.GONE);
            return;
        }

        tagFlowLayout.setVisibility(View.VISIBLE);

        for (int pos = 0; pos < imageUrlList.size(); pos++) {
            String url = imageUrlList.get(pos).getUrl();
            View item = mInflater
                    .inflate(R.layout.widget_review_image, tagFlowLayout, false);

            ImageView ivImage = item.findViewById(R.id.iv_image);

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.placeholder_loading_large)
                    .error(R.drawable.placeholder_error_large);

            TyUtils.getGlideRequest(
                    mFragment.get(),
                    mContext.get(),
                    url,
                    requestOptions,
                    ivImage);

            final int finalPos = pos;
            ivImage.setOnClickListener(v -> {
                setOnClickForReviewPic(imageUrlList, finalPos);
            });

            // 将 item 添加进流式布局
            tagFlowLayout.addView(item);
        }

    }

    /**
     * 点击评论图片
     *
     * @param imageUrlList 图片url列表
     * @param pos          下标
     */
    private void setOnClickForReviewPic(List<ArticleReviewResp.ImageUrlBean> imageUrlList, int pos) {
        if (mListener == null) {
            return;
        }

        ArrayList<WatchImageVO> imageList = new ArrayList<>();

        for (ArticleReviewResp.ImageUrlBean imageUrl : imageUrlList) {
            WatchImageVO watchImageVO = new WatchImageVO();
            watchImageVO.setBlurryUrl(imageUrl.getUrl());
            watchImageVO.setOriginalUrl(imageUrl.getOriginalUrl());

            imageList.add(watchImageVO);
        }
        mListener.onShowCommentImage(imageList, pos);
    }

    /**
     * 创建文章标签
     */
    private void createTag(List<LabelResp.LabelItem> list,
                           TagFlowLayout tagFlowLayout) {
        tagFlowLayout.removeAllViews();

        if (list != null && list.size() > 0) {

            for (LabelResp.LabelItem label : list) {
                TextView textView = (TextView) mInflater
                        .inflate(R.layout.item_label, tagFlowLayout, false);

                textView.setText(label.getName());
                tagFlowLayout.addView(textView);
            }

            tagFlowLayout.setVisibility(View.VISIBLE);
        } else {
            tagFlowLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 设置播放状态
     */
    private void setAudioStatus(MediaInfo mediaInfo, AudioContentViewHolder audioViewHolder) {
        // 设置总时间 和 播放时长
        setTime(mediaInfo, audioViewHolder);
        // 设置进度
        setProgress(mediaInfo, audioViewHolder);

        switch (mediaInfo.getStatus()) {
            case MediaInfo.NO_INIT:
                audioViewHolder.ivControlBtn.setSelected(false);
                break;

            case MediaInfo.INIT:
                audioViewHolder.ivControlBtn.setSelected(true);
                break;

            case MediaInfo.PLAY:
                audioViewHolder.ivControlBtn.setSelected(true);
                break;

            case MediaInfo.PAUSE:
                audioViewHolder.ivControlBtn.setSelected(false);
                break;

            case MediaInfo.ERROR:
                audioViewHolder.ivControlBtn.setSelected(false);
                break;

            case MediaInfo.ENDED:
                audioViewHolder.ivControlBtn.setSelected(false);
                break;
        }
    }

    /**
     * 设置进度
     */
    private void setProgress(MediaInfo mediaInfo, AudioContentViewHolder audioViewHolder) {
        if (mediaInfo.getDuration() == MediaInfo.NONE) {
            audioViewHolder.sbProcess.setDuration(0);
        } else {
            audioViewHolder.sbProcess.setDuration(mediaInfo.getDuration());
        }

        if (mediaInfo.getCurPos() == MediaInfo.NONE) {
            audioViewHolder.sbProcess.setPosition(0);
        } else {
            audioViewHolder.sbProcess.setPosition(mediaInfo.getCurPos());
        }

        audioViewHolder.sbProcess.setBufferedPosition(mediaInfo.getBuffered());
    }

    private void setTime(MediaInfo mediaInfo, AudioContentViewHolder audioViewHolder) {
        if (mediaInfo.getDuration() == MediaInfo.NONE
                || mediaInfo.getCurPos() == MediaInfo.NONE) {
            audioViewHolder.tvTotalTime.setVisibility(View.GONE);
            audioViewHolder.tvCurTime.setVisibility(View.GONE);
        } else {
            audioViewHolder.tvTotalTime.setText(TyUtils.formatProcessTime(mediaInfo.getDuration()));
            audioViewHolder.tvCurTime.setText(TyUtils.formatProcessTime(mediaInfo.getCurPos()));

            audioViewHolder.tvTotalTime.setVisibility(View.VISIBLE);
            audioViewHolder.tvCurTime.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof AudioContentViewHolder) {

            AudioContentViewHolder audioViewHolder = (AudioContentViewHolder) holder;

            ArticleAudioVO articleAudioVO =
                    (ArticleAudioVO) mData.get(audioViewHolder.getAdapterPosition()).getData();
            MediaInfo mediaInfo = articleAudioVO.getMediaInfo();

            mediaInfo.setListener(new JerryStatusListener() {
                @Override
                public void playStatusChange() {
                    setAudioStatus(mediaInfo, audioViewHolder);
                }

                @Override
                public void progressChange() {
                    setTime(mediaInfo, audioViewHolder);
                    setProgress(mediaInfo, audioViewHolder);
                }

                @Override
                public void tipChange(int code, String msg) {
                    setTime(mediaInfo, audioViewHolder);
                    audioViewHolder.ivControlBtn.setSelected(false);
                    Log.e(JerryAudioController.TAG, "tipChange: " + msg);

                }
            });
        } else if (holder instanceof AdvertisementViewHolder) {
            AdvertisementViewHolder advertisementViewHolder = (AdvertisementViewHolder) holder;
            advertisementViewHolder.llAdvertisement.addView(mWebView.getWebView(), 0);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof AudioContentViewHolder) {
            ArticleAudioVO articleAudioVO =
                    (ArticleAudioVO) mData.get(holder.getAdapterPosition()).getData();
            articleAudioVO.getMediaInfo().setListener(null);
        } else if (holder instanceof AdvertisementViewHolder) {
            AdvertisementViewHolder advertisementViewHolder = (AdvertisementViewHolder) holder;
            WebView webView = (WebView) advertisementViewHolder.llAdvertisement.getChildAt(0);
            mWebView.saveWebView(webView);
            advertisementViewHolder.llAdvertisement.removeAllViews();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public WebViewCache getWebViewCache() {
        return mWebView;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }


}
