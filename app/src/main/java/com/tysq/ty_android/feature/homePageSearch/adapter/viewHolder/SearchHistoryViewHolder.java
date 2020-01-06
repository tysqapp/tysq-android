package com.tysq.ty_android.feature.homePageSearch.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.widget.TagFlowLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchHistoryViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_search_history)
    public TextView tvSearchHistory;
    @BindView(R.id.flow_layout)
    public TagFlowLayout flowLayout;
    public SearchHistoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
