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
 * time         : 2019/5/22 上午10:01
 * desc         : 二级评论——项
 * version      : 1.3.0
 */
public class SubReviewViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_photo)
    public ImageView ivPhoto;
    @BindView(R.id.tv_sender_name)
    public TextView tvSenderName;
    @BindView(R.id.iv_arrow)
    public ImageView ivArrow;
    @BindView(R.id.tv_receiver_name)
    public TextView tvReceiverName;
    @BindView(R.id.tv_content)
    public TextView tvContent;
    @BindView(R.id.tv_time)
    public TextView tvTime;
    @BindView(R.id.iv_review)
    public ImageView ivReview;
    @BindView(R.id.iv_delete)
    public ImageView ivDelete;
    @BindView(R.id.v_divider)
    public View vDivider;
    @BindView(R.id.tag_flow)
    public TagFlowLayout tagFlowLayout;

    public SubReviewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}