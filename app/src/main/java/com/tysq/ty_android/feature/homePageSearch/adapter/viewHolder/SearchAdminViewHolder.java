package com.tysq.ty_android.feature.homePageSearch.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tysq.ty_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdminViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_photo)
    public ImageView ivPhoto;
    @BindView(R.id.tv_name)
    public TextView tvName;
    @BindView(R.id.iv_lv)
    public ImageView ivLv;
    @BindView(R.id.tv_job)
    public TextView tvJob;
    @BindView(R.id.tv_type)
    public TextView tvType;
    @BindView(R.id.tv_address)
    public TextView tvAddress;
    @BindView(R.id.tv_introduction)
    public TextView tvIntroduction;
    @BindView(R.id.rl_search_admin)
    public RelativeLayout llSearchAdmin;
    @BindView(R.id.v_divider_1)
    public View divider1;
    @BindView(R.id.v_divider_2)
    public View divider2;
    @BindView(R.id.rl_admin_description)
    public RelativeLayout rlAdminDecription;

    public SearchAdminViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
