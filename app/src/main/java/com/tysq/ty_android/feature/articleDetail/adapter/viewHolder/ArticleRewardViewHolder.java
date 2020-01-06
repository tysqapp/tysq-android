package com.tysq.ty_android.feature.articleDetail.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.widget.TagFlowLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleRewardViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_article_reward)
    public TextView tvArticleReward;
    @BindView(R.id.tv_reward_people_num)
    public TextView tvRewardNum;
    @BindView(R.id.rl_reward_list)
    public LinearLayout rlRewardList;
    @BindView(R.id.tag_flow)
    public TagFlowLayout tagFlowLayout;
    @BindView(R.id.iv_more)
    public ImageView ivMore;
    @BindView(R.id.rl_reward_btn)
    public RelativeLayout rlRewardBtn;
    public ArticleRewardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }
}
