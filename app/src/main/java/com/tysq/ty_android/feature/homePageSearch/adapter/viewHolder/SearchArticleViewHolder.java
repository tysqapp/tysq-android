package com.tysq.ty_android.feature.homePageSearch.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.widget.TagFlowLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchArticleViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rl_item)
    public RelativeLayout rlItem;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.flow_layout)
    public TagFlowLayout flowLayout;
    @BindView(R.id.tv_author)
    public TextView tvAuthor;
    @BindView(R.id.tv_time)
    public TextView tvTime;
    @BindView(R.id.tv_review_count)
    public TextView tvReviewCount;
    @BindView(R.id.tv_read_count)
    public TextView tvReadCount;

    public SearchArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
