package com.tysq.ty_android.feature.articleDetail.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.widget.TagFlowLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author       : frog
 * time         : 2019/5/20 下午2:10
 * desc         : 评论——一级
 * version      : 1.3.0
 */
public class ReviewTopViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_photo)
    public ImageView ivPhoto;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.tv_content)
    public TextView tvContent;
    @BindView(R.id.tv_time)
    public TextView tvTime;
    @BindView(R.id.iv_review)
    public ImageView ivReview;
    @BindView(R.id.iv_delete)
    public ImageView ivDelete;
    @BindView(R.id.tag_flow)
    public TagFlowLayout tagFlowLayout;

    public ReviewTopViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}