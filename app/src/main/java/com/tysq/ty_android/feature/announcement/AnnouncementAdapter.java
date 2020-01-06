package com.tysq.ty_android.feature.announcement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.adapter.CommonSimpleAdapter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.utils.TyUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.AdResp;

/**
 * author       : frog
 * time         : 2019-08-20 16:12
 * desc         : 公告
 * version      : 1.3.0
 */
public class AnnouncementAdapter
        extends CommonSimpleAdapter<AdResp.AdvertisementListBean, AnnouncementAdapter.ViewHolder> {

    public AnnouncementAdapter(Context context, List<AdResp.AdvertisementListBean> adRespList) {
        super(context, adRespList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                mInflater.inflate(R.layout.item_announcement, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        List<Integer> bannerBgResList = Constant.BANNER_BG_RES;
        holder.ivBg.setImageResource(bannerBgResList.get(position % bannerBgResList.size()));

        final AdResp.AdvertisementListBean item = mDataList.get(position);
        if (TextUtils.isEmpty(item.getUrl())) {
            holder.ivGoTo.setVisibility(View.GONE);
            holder.rlBannerItem.setClickable(false);
        } else {
            holder.ivGoTo.setVisibility(View.VISIBLE);
            holder.rlBannerItem.setClickable(true);
            holder.rlBannerItem.setOnClickListener(v -> {

//                String articleId = TyUtils.getArticleId(item.getUrl());
//                if (TextUtils.isEmpty(articleId)) {
//                    TyWebViewActivity.startActivity(mContext.get(), item.getUrl());
//                } else {
//                    ArticleDetailActivity.startActivity(mContext.get(), articleId);
//                }

                TyUtils.handleWebViewLink(mContext.get(), item.getUrl());

            });
        }

        holder.tvContent.setText(item.getTitle());

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_banner_item)
        RelativeLayout rlBannerItem;
        @BindView(R.id.iv_bg)
        ImageView ivBg;
        @BindView(R.id.iv_go_to)
        ImageView ivGoTo;
        @BindView(R.id.tv_content)
        TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
