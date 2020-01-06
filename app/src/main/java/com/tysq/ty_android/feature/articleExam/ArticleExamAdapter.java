package com.tysq.ty_android.feature.articleExam;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bit.utils.DateUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.adapter.CommonHeaderSimpleAdapter;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.article.MyArticleResp;
import response.article.ReviewArticleListResp;
import response.common.TitleCountVO;

/**
 * author       : frog
 * time         : 2019-08-28 11:46
 * desc         : 文章审核列表
 * version      : 1.0.0
 */
public class ArticleExamAdapter
        extends CommonHeaderSimpleAdapter<TitleCountVO, ReviewArticleListResp.ReviewArticlesBean> {

    public ArticleExamAdapter(Context context,
                              TitleCountVO header,
                              List<ReviewArticleListResp.ReviewArticlesBean> reviewArticlesBeanList) {
        super(context, header, reviewArticlesBeanList);
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HeadViewHolder(
                mInflater.inflate(R.layout.item_common_count_title, parent, false)
        );
    }

    @Override
    protected RecyclerView.ViewHolder getContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(
                mInflater.inflate(R.layout.item_article_exam, parent, false)
        );
    }

    @Override
    protected RecyclerView.ViewHolder getEmptyViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {

            ReviewArticleListResp.ReviewArticlesBean reviewArticlesBean
                    = mDataList.get(position - getHeaderCount());

            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder.tvGoToExam.setOnClickListener(v -> {
                ArticleDetailActivity.startActivity(mContext.get(),
                        reviewArticlesBean.getArticleId());
            });

            contentViewHolder.tvPublisher.setText(reviewArticlesBean.getAuthor());
            contentViewHolder.tvTitle.setText(reviewArticlesBean.getTitle());
            contentViewHolder.tvTime
                    .setText(DateUtils.getM_D_H_MViaTimeStamp(reviewArticlesBean.getUpdatedAt() * 1000L));

        } else if (holder instanceof HeadViewHolder) {

            HeadViewHolder headViewHolder = (HeadViewHolder) holder;

            String totalNum = mContext.get().getString(R.string.exam_total_num);
            String totalContent = String.format(totalNum, mHeader.getCount());

            headViewHolder.tvTitle.setText(totalContent);

        }
    }

    static class HeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_go_to_exam)
        TextView tvGoToExam;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_publisher)
        TextView tvPublisher;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ContentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
