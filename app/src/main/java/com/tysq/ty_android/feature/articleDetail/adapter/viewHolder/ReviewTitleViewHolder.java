package com.tysq.ty_android.feature.articleDetail.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tysq.ty_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author       : frog
 * time         : 2019/5/20 下午2:12
 * desc         : 评论标题
 * version      : 1.3.0
 */
public class ReviewTitleViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_title)
    public TextView tvTitle;

    public ReviewTitleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}