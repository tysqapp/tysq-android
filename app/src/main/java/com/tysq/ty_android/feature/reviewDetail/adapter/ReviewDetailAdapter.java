package com.tysq.ty_android.feature.reviewDetail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.view.fragment.BitBaseFragment;
import com.bumptech.glide.request.RequestOptions;
import com.jerry.image_watcher.model.WatchImageVO;
import com.tysq.ty_android.R;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.ReviewEmptyViewHolder;
import com.tysq.ty_android.feature.articleDetail.adapter.viewHolder.ReviewTopViewHolder;
import com.tysq.ty_android.feature.articleDetail.config.ArticleConstants;
import com.tysq.ty_android.feature.articleDetail.listener.ArticleDetailListener;
import com.tysq.ty_android.feature.reviewDetail.adapter.viewHolder.ReviewDetailSubViewHolder;
import com.tysq.ty_android.feature.reviewDetail.adapter.viewHolder.ReviewDetailTitleViewHolder;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.utils.TyUtils;
import com.tysq.ty_android.widget.TagFlowLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import response.article.ArticleReviewResp;

/**
 * author       : frog
 * time         : 2019/5/23 下午2:59
 * desc         : 评论详情
 * version      : 1.3.0
 */
public class ReviewDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 多出的两项：一级评论、评论头
    public static final int HEAD_COUNT = 2;
    // 评论回复 的 索引
    public static final int REVIEW_TITLE = 1;

    private static final int NANO = 1000_000_000;

    private ArticleReviewResp.ArticleCommentsBean mTopData;
    private List<ArticleReviewResp.ArticleCommentsBean.ReplyBean> mSubData;
    private WeakReference<BitBaseFragment> mFragment;
    private WeakReference<Context> mContext;
    private LayoutInflater mLayoutInflater;

    private ArticleDetailListener mListener;

    public ReviewDetailAdapter(BitBaseFragment fragment,
                               Context context) {
        this.mFragment = new WeakReference<>(fragment);
        this.mContext = new WeakReference<>(context);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void setTopData(ArticleReviewResp.ArticleCommentsBean mTopData) {
        this.mTopData = mTopData;
    }

    public void setSubData(List<ArticleReviewResp.ArticleCommentsBean.ReplyBean> mSubData) {
        this.mSubData = mSubData;
    }

    public void setListener(ArticleDetailListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == ArticleConstants.REVIEW_TOP_ITEM) {
            viewHolder = new ReviewTopViewHolder(
                    mLayoutInflater.inflate(R.layout.item_article_detail_review_top,
                            parent, false));

        } else if (viewType == ArticleConstants.REVIEW_TITLE) {
            viewHolder = new ReviewDetailTitleViewHolder(
                    mLayoutInflater.inflate(R.layout.item_review_detail_sub_title,
                            parent, false));

        } else if (viewType == ArticleConstants.REVIEW_SUB_ITEM) {
            viewHolder = new ReviewDetailSubViewHolder(
                    mLayoutInflater.inflate(R.layout.item_review_detail_sub,
                            parent, false));

        } else if (viewType == ArticleConstants.REVIEW_SUB_EMPTY) {
            viewHolder = new ReviewEmptyViewHolder(
                    mLayoutInflater.inflate(R.layout.item_article_detail_review_empty,
                            parent, false));

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ReviewTopViewHolder) {        //一级评论

            ReviewTopViewHolder reviewTopViewHolder = (ReviewTopViewHolder) holder;

            if (TextUtils.isEmpty(mTopData.getContent())) {
                reviewTopViewHolder.tvContent.setVisibility(View.GONE);
            } else {
                reviewTopViewHolder.tvContent.setVisibility(View.VISIBLE);
                reviewTopViewHolder.tvContent.setText(mTopData.getContent());
            }

            reviewTopViewHolder.tvTitle.setText(mTopData.getCommentatorName());
            reviewTopViewHolder.tvTime.setText(
                    TyUtils.getPublishTime(mTopData.getTime())
            );

            TyUtils.initUserPhoto(
                    mFragment.get(),
                    mContext.get(),
                    mTopData.getIconUrl(),
                    reviewTopViewHolder.ivPhoto);

            // 添加禁止评论权限
            if (mListener != null) {
                reviewTopViewHolder.ivPhoto.setOnClickListener(v -> {
                    mListener.onShowUserAdmin(mTopData.getCommentatorId(),
                            mTopData.getIconUrl(),
                            mTopData.getCommentatorName(),
                            mTopData.getCommentatorGrade());
                });

                reviewTopViewHolder.tvTitle.setOnClickListener(v -> {
                    mListener.onShowUserAdmin(mTopData.getCommentatorId(),
                            mTopData.getIconUrl(),
                            mTopData.getCommentatorName(),
                            mTopData.getCommentatorGrade());
                });
            }

            createReviewImage(reviewTopViewHolder.tagFlowLayout, mTopData.getImageUrl());

            boolean isHaveDeletePermission = false;
            if (mTopData.isCanDelete()) {
                isHaveDeletePermission = true;
            } else if (!UserCache.isEmpty() &&
                    UserCache.getDefault().getAccountId() == mTopData.getCommentatorId()) {
                isHaveDeletePermission = true;
            }

            if (isHaveDeletePermission) {
                reviewTopViewHolder.ivDelete.setVisibility(View.VISIBLE);
                // 删除评论
                reviewTopViewHolder.ivDelete.setOnClickListener(v -> {
                    new MaterialDialog.Builder(mContext.get())
                            .title(mContext.get().getString(R.string.edit_quit_title))
                            .content(mContext.get().getString(R.string.edit_content_clear_tip))
                            .negativeColor(ContextCompat.getColor(mContext.get(), R.color.et_tip_text_color))
                            .negativeText(mContext.get().getString(R.string.common_confirm))
                            .onNegative((dialog, which) -> {
                                if (mListener == null) {
                                    return;
                                }

                                mListener.onDeleteCommentItem(v, holder.getAdapterPosition(),
                                        mTopData.getId(), mTopData.getId(), true);
                            })
                            .positiveColor(ContextCompat.getColor(mContext.get(), R.color.main_blue_color))
                            .positiveText(mContext.get().getString(R.string.common_cancel))
                            .show();
                });
            } else {
                reviewTopViewHolder.ivDelete.setVisibility(View.GONE);
            }

            reviewTopViewHolder.ivReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener == null) {
                        return;
                    }

                    mListener.onReplyComment(v,
                            holder.getAdapterPosition(),         // 位置
                            mTopData.getArticleId(),           // 文章id
                            mTopData.getCommentatorId(),       // 评论者id
                            mTopData.getCommentatorName(),     // 评论者的名字
                            mTopData.getId(),                  // 该条评论的id
                            mTopData.getId());                 // 一级评论的id
                }
            });


        } else if (holder instanceof ReviewDetailTitleViewHolder) {

            ReviewDetailTitleViewHolder reviewDetailTitleViewHolder
                    = (ReviewDetailTitleViewHolder) holder;

            reviewDetailTitleViewHolder.tvReviewCount
                    .setText(String.format(mContext.get().getString(R.string.review_detail_count),
                            mSubData == null ? 0 : mSubData.size()));

        } else if (holder instanceof ReviewDetailSubViewHolder) {

            ReviewDetailSubViewHolder subReviewViewHolder = (ReviewDetailSubViewHolder) holder;

            ArticleReviewResp.ArticleCommentsBean.ReplyBean replyBean
                    = mSubData.get(position - HEAD_COUNT);

            createReviewImage(subReviewViewHolder.tagFlow, replyBean.getImageUrl());

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
                            .title(mContext.get().getString(R.string.article_detail_delete_confirm))
                            .negativeColor(ContextCompat.getColor(mContext.get(), R.color.et_tip_text_color))
                            .negativeText(mContext.get().getString(R.string.common_confirm))
                            .onNegative((dialog, which) -> {
                                if (mListener == null) {
                                    return;
                                }

                                mListener.onDeleteCommentItem(v, holder.getAdapterPosition(),
                                        replyBean.getId(), replyBean.getParentId(), false);
                            })
                            .positiveColor(ContextCompat.getColor(mContext.get(), R.color.main_blue_color))
                            .positiveText(mContext.get().getString(R.string.common_cancel))
                            .show();
                });
            } else {
                subReviewViewHolder.ivDelete.setVisibility(View.GONE);
            }

            // 添加禁止评论权限
            if (mListener != null) {
                subReviewViewHolder.ivPhoto.setOnClickListener(v -> {
                    mListener.onShowUserAdmin(
                            replyBean.getCommentatorId(),
                            replyBean.getIconUrl(),
                            replyBean.getCommentatorName(),
                            replyBean.getCommentatorGrade()
                    );
                });

                subReviewViewHolder.tvReceiverName.setOnClickListener(v -> {
                    mListener.onShowUserAdmin(
                            replyBean.getCommentedId(),
                            replyBean.getCommentedIcon(),
                            replyBean.getCommentedName(),
                            replyBean.getCommentedGrade()
                    );
                });

                subReviewViewHolder.tvSenderName.setOnClickListener(v -> {
                    mListener.onShowUserAdmin(
                            replyBean.getCommentatorId(),
                            replyBean.getIconUrl(),
                            replyBean.getCommentatorName(),
                            replyBean.getCommentatorGrade()
                    );
                });
            }

            subReviewViewHolder.ivReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onReplyComment(v,
                            holder.getAdapterPosition() - HEAD_COUNT,        // 位置
                            replyBean.getArticleId(),           // 文章id
                            replyBean.getCommentatorId(),       // 评论者id
                            replyBean.getCommentatorName(),     // 评论者的名字
                            replyBean.getId(),                  // 该条评论的id
                            replyBean.getParentId());           // 一级评论的id
                }
            });

            if (TextUtils.isEmpty(replyBean.getContent())) {
                subReviewViewHolder.tvContent.setVisibility(View.GONE);
            } else {
                subReviewViewHolder.tvContent.setVisibility(View.VISIBLE);
                subReviewViewHolder.tvContent.setText(replyBean.getContent());
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

        } else if (holder instanceof ReviewEmptyViewHolder) {

        }

    }

    @Override
    public int getItemCount() {
        if (mTopData == null) {
            return 0;
        }

        int size = mSubData == null ? 0 : mSubData.size();
        return size == 0 ? HEAD_COUNT + 1 : HEAD_COUNT + size;
    }

    @Override
    public int getItemViewType(int position) {

        switch (position) {
            case 0:
                return ArticleConstants.REVIEW_TOP_ITEM;
            case 1:
                return ArticleConstants.REVIEW_TITLE;
            default: {
                if (mSubData.size() <= 0) {
                    return ArticleConstants.REVIEW_SUB_EMPTY;
                } else {
                    return ArticleConstants.REVIEW_SUB_ITEM;
                }
            }
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
            ArticleReviewResp.ImageUrlBean imageUrlBean = imageUrlList.get(pos);

            View item = mLayoutInflater
                    .inflate(R.layout.widget_review_image, tagFlowLayout, false);

            ImageView ivImage = item.findViewById(R.id.iv_image);

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.placeholder_loading_large)
                    .error(R.drawable.placeholder_error_large);

            TyUtils.getGlideRequest(
                    mFragment.get(),
                    mContext.get(),
                    imageUrlBean.getUrl(),
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


}
