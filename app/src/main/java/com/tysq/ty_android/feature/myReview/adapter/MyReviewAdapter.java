package com.tysq.ty_android.feature.myReview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.utils.DateUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.bumptech.glide.request.RequestOptions;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.adapter.CommonHeaderSimpleAdapter;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import com.tysq.ty_android.feature.myReview.MyReviewListener;
import com.tysq.ty_android.utils.TyUtils;
import com.tysq.ty_android.widget.TagFlowLayout;
import com.zinc.jrecycleview.config.JRecycleConfig;
import com.zinc.jrecycleview.swipe.JSwipeViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.MyCommentListResp;
import response.article.ArticleReviewResp;
import response.common.TitleCountVO;

/**
 * author       : frog
 * time         : 2019/5/26 下午2:44
 * desc         : 我的评论
 * version      : 1.3.0
 */
public class MyReviewAdapter
        extends CommonHeaderSimpleAdapter<TitleCountVO, MyCommentListResp.CommentInfoBean> {

    private final WeakReference<MyReviewListener> mListener;
    private final WeakReference<BitBaseFragment> mFragment;

    private final int ARTICLE_NAME_COLOR;
    private final boolean mIsNeedSwipe;

    public MyReviewAdapter(BitBaseFragment fragment,
                           Context context,
                           TitleCountVO titleCountVO,
                           MyReviewListener listener,
                           List<MyCommentListResp.CommentInfoBean> dataList,
                           boolean isNeedRefresh,
                           boolean isNeedSwipe) {

        super(context, titleCountVO, dataList, isNeedRefresh);

        this.mFragment = new WeakReference<>(fragment);

        this.ARTICLE_NAME_COLOR = ContextCompat.getColor(context, R.color.orange_color);

        this.mListener = new WeakReference<>(listener);
        this.mIsNeedSwipe = isNeedSwipe;
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TitleViewHolder(
                mInflater
                        .inflate(R.layout.item_common_count_title, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(
                mInflater
                        .inflate(JRecycleConfig.SWIPE_LAYOUT, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getEmptyViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;

            String format = String.format(mContext.get()
                    .getString(R.string.my_review_count), mHeader.getCount());

            titleViewHolder.tvTitle.setText(format);
        } else if (holder instanceof ContentViewHolder) {
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;

            MyCommentListResp.CommentInfoBean commentInfoBean
                    = mDataList.get(position - getHeaderCount());

            contentViewHolder.getSwipeItemLayout().setSwipeEnable(mIsNeedSwipe);

            setTip(commentInfoBean, contentViewHolder.tvTip);

            if (TextUtils.isEmpty(commentInfoBean.getContent())) {
                contentViewHolder.tvContent.setVisibility(View.GONE);
            } else {
                contentViewHolder.tvContent.setVisibility(View.VISIBLE);
                contentViewHolder.tvContent.setText(commentInfoBean.getContent());
            }

            createImage(commentInfoBean, contentViewHolder.tagFlow);

            setCommentedContent(commentInfoBean,
                    contentViewHolder.tvCommentedContent,
                    contentViewHolder.ivCommentedContent);

            contentViewHolder.tvCommentTime
                    .setText(DateUtils.getTimeStringViaTimestamp(commentInfoBean.getTime() * 1000L));

            contentViewHolder.llItem.setOnClickListener(
                    v -> ArticleDetailActivity.startActivity(mContext.get(),
                            commentInfoBean.getArticleId(),
                            commentInfoBean.getId())
            );

            contentViewHolder.tvDelete.setOnClickListener(
                    v -> {
                        if (mListener.get() != null) {
                            contentViewHolder.getSwipeItemLayout().close();
                            mListener.get().deleteReviewItem(commentInfoBean.getId());
                        }
                    }
            );

        }

    }

    private void createImage(MyCommentListResp.CommentInfoBean commentInfoBean,
                             TagFlowLayout tagFlow) {

        List<ArticleReviewResp.ImageUrlBean> imageUrl = commentInfoBean.getImageUrl();

        if (imageUrl == null || imageUrl.size() <= 0) {
            tagFlow.setVisibility(View.GONE);
            return;
        }

        tagFlow.removeAllViews();
        tagFlow.setVisibility(View.VISIBLE);

        for (ArticleReviewResp.ImageUrlBean imageUrlBean : imageUrl) {
            View item = mInflater
                    .inflate(R.layout.widget_review_image, tagFlow, false);

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

            // 将 item 添加进流式布局
            tagFlow.addView(item);
        }

    }

    private void setTip(MyCommentListResp.CommentInfoBean data, TextView tvTip) {

        int start = -1;
        int end = -1;

        String formatString;

        if (data.getCommentedId() == 0) {     // 评论文章
            String string = mContext.get().getString(R.string.my_review_tip_to_article);
            formatString = String.format(string, data.getTitle());
        } else {                              // 评论别人
            String string = mContext.get().getString(R.string.my_review_tip_to_review);
            formatString = String.format(string, data.getCommentedName(), data.getTitle());
        }

        start = formatString.lastIndexOf(data.getTitle());
        end = start + data.getTitle().length();

        if (start == -1 || end == -1 || start <= 0) {
            return;
        }

        ForegroundColorSpan span = new ForegroundColorSpan(ARTICLE_NAME_COLOR);
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(formatString);

        try {
            builder.setSpan(span,
                    start - 1,
                    start + data.getTitle().length() + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvTip.setText(builder);

    }

    /**
     * 设置被回复的内容
     */
    private void setCommentedContent(MyCommentListResp.CommentInfoBean data,
                                     TextView tvContent,
                                     ImageView ivContent) {

        if (data.getCommentedId() == 0) {
            tvContent.setVisibility(View.GONE);
            ivContent.setVisibility(View.GONE);
            return;
        }

        tvContent.setVisibility(View.VISIBLE);
        ivContent.setVisibility(View.VISIBLE);

        String commentedName = data.getCommentedName();
        String respondContent = data.getRespondContent();

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(commentedName);
        builder.append("：");
        builder.append(respondContent);

        tvContent.setText(builder);
    }

    public static class ContentViewHolder extends JSwipeViewHolder {
        LinearLayout llItem;
        TextView tvTip;
        TextView tvContent;
        TagFlowLayout tagFlow;
        ImageView ivCommentedContent;
        TextView tvCommentedContent;
        TextView tvCommentTime;

        TextView tvDelete;

        ContentViewHolder(View view) {
            super(view);
        }

        @Override
        public int getRightMenuLayout() {
            return R.layout.menu_right_delete;
        }

        @Override
        public int getContentLayout() {
            return R.layout.item_my_review_content;
        }

        @Override
        public void initItem(FrameLayout frameLayout) {
            tvDelete = frameLayout.findViewById(R.id.tv_delete);

            llItem = frameLayout.findViewById(R.id.ll_item);
            tvTip = frameLayout.findViewById(R.id.tv_tip);
            tvContent = frameLayout.findViewById(R.id.tv_content);
            tagFlow = frameLayout.findViewById(R.id.tag_flow);
            ivCommentedContent = frameLayout.findViewById(R.id.iv_commented_content);
            tvCommentedContent = frameLayout.findViewById(R.id.tv_commented_content);
            tvCommentTime = frameLayout.findViewById(R.id.tv_comment_time);
        }

    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
