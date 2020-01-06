package com.tysq.ty_android.feature.articleDetail.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tysq.ty_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author       : frog
 * time         : 2019/5/20 下午2:14
 * desc         : 内容——视频
 * version      : 1.3.0
 */
public class VideoContentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rl_video)
    public RelativeLayout rlVideo;

    @BindView(R.id.iv_cover)
    public ImageView ivCover;

    @BindView(R.id.iv_img_watcher)
    public ImageView ivImageWatcher;

    @BindView(R.id.iv_download)
    public ImageView ivDownload;

    public VideoContentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}