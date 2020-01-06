package com.tysq.ty_android.feature.articleDetail.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tysq.ty_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author       : frog
 * time         : 2019/5/22 上午10:01
 * desc         : 二级评论——更多
 * version      : 1.3.0
 */
public class SubReviewMoreViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_more)
    public TextView tvMore;

    public SubReviewMoreViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}