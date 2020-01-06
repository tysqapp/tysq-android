package com.tysq.ty_android.feature.myArticle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.utils.DateUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.adapter.CommonHeaderSimpleAdapter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import com.tysq.ty_android.utils.TyUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.article.MyArticleResp;
import vo.article.MyArticleTitleVO;

/**
 * author       : frog
 * time         : 2019/5/27 上午10:34
 * desc         : 我的文章
 * version      : 1.3.0
 */
public class MyArticleAdapter
        extends CommonHeaderSimpleAdapter<MyArticleTitleVO, MyArticleResp.ArticlesInfoBean> {

    private final WeakReference<MyArticleListener> mListener;

    public MyArticleAdapter(Context context,
                            List<MyArticleResp.ArticlesInfoBean> data,
                            MyArticleTitleVO titleCountVO,
                            MyArticleListener listener,
                            boolean isNeedHeader) {
        super(context, titleCountVO, data, isNeedHeader);
        this.mListener = new WeakReference<>(listener);
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TitleViewHolder(
                mInflater.inflate(R.layout.item_my_article_title, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(
                mInflater.inflate(R.layout.item_my_article_content, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getEmptyViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmptyViewHolder(
//                mInflater.inflate(R.layout.blank_empty_my_article, parent, false)
                mInflater.inflate(R.layout.blank_empty_tip, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TitleViewHolder) {

            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            String format = String.format(
                    mContext.get().getString(R.string.my_article_count),
                    mHeader.getCount());

            titleViewHolder.tvTitle.setText(format);

            Integer res = Constant.MY_ARTICLE_TYPE_NAME.get(mHeader.getType());
            if (res != null) {
                titleViewHolder.tvType.setText(mContext.get().getString(res));
            }

            titleViewHolder.tvType.setOnClickListener(v -> {
                if (mListener.get() != null) {
                    mListener.get().onTypeClick(v);
                }
            });

        } else if (holder instanceof ContentViewHolder) {

            MyArticleResp.ArticlesInfoBean articlesInfoBean
                    = mDataList.get(position - getHeaderCount());

            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;

            contentViewHolder.tvTime.setText(
                    DateUtils.getTimeStringViaTimestamp(articlesInfoBean.getCreatedTime() * 1000L));

            String readNum = TyUtils.formatNum(articlesInfoBean.getReadNumber());
            contentViewHolder.tvReadCount.setText(readNum);

            String commentNum = TyUtils.formatNum(articlesInfoBean.getCommentNumber());
            contentViewHolder.tvReviewCount.setText(commentNum);

            contentViewHolder.rlItem.setOnClickListener(v ->
                    ArticleDetailActivity.startActivity(mContext.get(),
                            articlesInfoBean.getId()));

            switch (articlesInfoBean.getStatus()) {
                case Constant.MyArticleType.PUBLISH:        // 发布
                    contentViewHolder.ivArticleState.setVisibility(View.GONE);

                    contentViewHolder.tvTitle.setText(articlesInfoBean.getTitle());

                    controlRefuseReason(contentViewHolder, false, "");
                    break;

                case Constant.MyArticleType.DRAFT:          // 草稿
                    contentViewHolder.ivArticleState.setVisibility(View.GONE);

                    TyUtils.addDraftTag(mContext.get(),
                            contentViewHolder.tvTitle,
                            articlesInfoBean.getTitle());

                    controlRefuseReason(contentViewHolder, false, "");
                    break;

                case Constant.MyArticleType.REFUSE:         // 驳回
                    contentViewHolder.ivArticleState.setVisibility(View.VISIBLE);
                    contentViewHolder.ivArticleState.setImageResource(R.drawable.ic_refuse);

                    contentViewHolder.tvTitle.setText(articlesInfoBean.getTitle());

                    controlRefuseReason(contentViewHolder, true, articlesInfoBean.getReason());
                    break;

                case Constant.MyArticleType.EXAM:           // 审核
                    contentViewHolder.ivArticleState.setVisibility(View.VISIBLE);
                    contentViewHolder.ivArticleState.setImageResource(R.drawable.ic_wait_examination);

                    contentViewHolder.tvTitle.setText(articlesInfoBean.getTitle());
                    controlRefuseReason(contentViewHolder, false, "");
                    break;

                case Constant.MyArticleType.HIDE:
                    contentViewHolder.ivArticleState.setVisibility(View.GONE);

                    TyUtils.addHideTag(mContext.get(),
                            contentViewHolder.tvTitle,
                            articlesInfoBean.getTitle());

                    controlRefuseReason(contentViewHolder, false, "");
                    break;
            }

        }

    }

    private void controlRefuseReason(ContentViewHolder contentViewHolder,
                                     boolean isShow,
                                     String reason) {

        if (isShow) {
            contentViewHolder.vDividerRefuseReason.setVisibility(View.VISIBLE);
            contentViewHolder.tvRefuseReason.setVisibility(View.VISIBLE);
            contentViewHolder.tvRefuseReason.setText(
                    String.format(mContext.get().getString(R.string.my_article_refuse_reason), reason)
            );
        } else {
            contentViewHolder.vDividerRefuseReason.setVisibility(View.GONE);
            contentViewHolder.tvRefuseReason.setVisibility(View.GONE);
        }

    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_type)
        TextView tvType;

        TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_item)
        RelativeLayout rlItem;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_review_img)
        ImageView ivReviewImg;
        @BindView(R.id.tv_review_count)
        TextView tvReviewCount;
        @BindView(R.id.iv_read_img)
        ImageView ivReadImg;
        @BindView(R.id.tv_read_count)
        TextView tvReadCount;
        @BindView(R.id.v_divider_refuse_reason)
        View vDividerRefuseReason;
        @BindView(R.id.tv_refuse_reason)
        TextView tvRefuseReason;
        @BindView(R.id.iv_article_state)
        ImageView ivArticleState;


        ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface MyArticleListener {
        void onTypeClick(View v);
    }

}
