package com.tysq.ty_android.feature.myAttention.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tysq.ty_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ll_my_attention)
    public RelativeLayout llMyAttention;
    @BindView(R.id.iv_photo)
    public ImageView ivPhoto;
    @BindView(R.id.tv_name)
    public TextView tvName;
    @BindView(R.id.iv_lv)
    public ImageView ivLv;
    @BindView(R.id.tv_introduction)
    public TextView tvIntroduction;
    @BindView(R.id.article_num)
    public TextView articleNum;
    @BindView(R.id.collect_num)
    public TextView collectNum;
    @BindView(R.id.read_num)
    public TextView readNum;
    @BindView(R.id.rl_focus_someone)
    public RelativeLayout rlFocusSomeOne;
    @BindView(R.id.iv_add)
    public ImageView ivAdd;
    @BindView(R.id.tv_focus_someone)
    public TextView tvFocusSomeOne;

    public ContentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
