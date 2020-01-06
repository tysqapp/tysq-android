package com.tysq.ty_android.feature.articleDetail.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.widget.TagFlowLayout;
import com.zinc.jrecycleview.stick.IStick;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author       : frog
 * time         : 2019/5/20 下午2:32
 * desc         : 头部
 * version      : 1.3.0
 */
public class TitleViewHolder extends RecyclerView.ViewHolder implements IStick {
    @BindView(R.id.iv_article_state)
    public ImageView ivArticleState;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.tag_flow)
    public TagFlowLayout tagFlow;
    @BindView(R.id.iv_photo)
    public ImageView ivPhoto;
    @BindView(R.id.tv_author)
    public TextView tvAuthor;
    @BindView(R.id.tv_time)
    public TextView tvTime;
    @BindView(R.id.iv_reading)
    public ImageView ivReading;
    @BindView(R.id.tv_reading)
    public TextView tvReading;
    @BindView(R.id.tv_reason)
    public TextView tvReason;
    @BindView(R.id.tv_column)
    public TextView tvColumn;

    public TitleViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}