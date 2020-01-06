package com.tysq.ty_android.feature.reviewDetail.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tysq.ty_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author       : frog
 * time         : 2019/5/23 下午3:28
 * desc         : 评论详情——回复头
 * version      : 1.3.0
 */
public class ReviewDetailTitleViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_review_count)
    public TextView tvReviewCount;

    public ReviewDetailTitleViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

}
