package com.tysq.ty_android.feature.articleDetail.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysq.ty_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author       : frog
 * time         : 2019/5/20 下午2:13
 * desc         : 相关推荐——内容
 * version      : 1.3.0
 */
public class RecommendContentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ll_item)
    public LinearLayout llItem;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.tv_review_count)
    public TextView tvReviewCount;
    @BindView(R.id.tv_read_count)
    public TextView tvReadCount;
    @BindView(R.id.tv_author)
    public TextView tvAuthor;
    @BindView(R.id.tv_time)
    public TextView tvTime;

    public RecommendContentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}