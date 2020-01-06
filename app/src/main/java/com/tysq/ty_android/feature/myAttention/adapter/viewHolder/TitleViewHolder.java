package com.tysq.ty_android.feature.myAttention.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tysq.ty_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TitleViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_title)
    public TextView tvTitle;

    public TitleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
