package com.tysq.ty_android.feature.articleDetail.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerry.media.widget.DefaultTimeBar;
import com.tysq.ty_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author       : frog
 * time         : 2019/5/20 下午2:31
 * desc         : 内容——音频
 * version      : 1.3.0
 */
public class AudioContentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_control_btn)
    public ImageView ivControlBtn;
    @BindView(R.id.tv_audio_name)
    public TextView tvAudioName;
    @BindView(R.id.sb_process)
    public DefaultTimeBar sbProcess;
    @BindView(R.id.tv_cur_time)
    public TextView tvCurTime;
    @BindView(R.id.tv_total_time)
    public TextView tvTotalTime;

    public AudioContentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}