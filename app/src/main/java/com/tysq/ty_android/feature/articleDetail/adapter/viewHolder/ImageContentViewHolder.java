package com.tysq.ty_android.feature.articleDetail.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.tysq.ty_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author       : frog
 * time         : 2019/5/20 下午2:31
 * desc         : 内容——图片
 * version      : 1.3.0
 */
public class ImageContentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_img)
    public ImageView ivImg;

    public ImageContentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

