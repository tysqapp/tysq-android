package com.tysq.ty_android.feature.articleDetail.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tysq.ty_android.R;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.utils.ScreenAdapterUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author       : liaozhenlin
 * time         : 2019/10/24 0024 22:37
 * desc         : 广告位
 * version      : 1.5.0
 */
public class AdvertisementViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ll_advertisement)
    public LinearLayout llAdvertisement;
    public AdvertisementViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }
}
