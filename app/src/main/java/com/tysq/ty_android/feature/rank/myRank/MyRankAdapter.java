package com.tysq.ty_android.feature.rank.myRank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.utils.DateUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.adapter.CommonHeaderSimpleAdapter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.rank.orderRank.OrderRankActivity;
import com.tysq.ty_android.feature.web.TyWebViewActivity;
import com.zinc.lib_jerry_editor.span.typeface.JerryBoldSpan;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.rank.MyRankResp;
import response.rank.RankDetailResp;

/**
 * author       : frog
 * time         : 2019-07-15 18:24
 * desc         : 我的积分
 * version      : 1.3.0
 */
public class MyRankAdapter extends CommonHeaderSimpleAdapter<MyRankResp, RankDetailResp.ScoresListBean> {

    private final DecimalFormat df = new DecimalFormat("#,###");

    public MyRankAdapter(Context context,
                         MyRankResp myRankResp,
                         List<RankDetailResp.ScoresListBean> detailData) {
        super(context, myRankResp, detailData);
    }

    /**
     * 获取 头部的ViewHolder
     */
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull ViewGroup parent,
                                                          int viewType) {
        return new HeadViewHolder(mInflater
                .inflate(R.layout.item_my_rank_title, parent, false));
    }

    /**
     * 获取 内容的ViewHolder
     */
    protected RecyclerView.ViewHolder getContentViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        return new ContentViewHolder(mInflater
                .inflate(R.layout.item_my_rank_detail, parent, false));
    }

    /**
     * 获取 空的ViewHolder
     */
    protected RecyclerView.ViewHolder getEmptyViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {
        return new EmptyViewHolder(mInflater
                .inflate(R.layout.blank_empty_half_tip, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {

            HeadViewHolder headViewHolder = (HeadViewHolder) holder;

            // 积分订单
            headViewHolder.tvRankOrder.setOnClickListener(v -> {
//                DataSourceSettingActivity.startActivity(mContext.get(), OrderRankFragment.TAG);
                OrderRankActivity.startActivity(mContext.get());
            });

            // 兑换积分
            headViewHolder.tvRankExchange.setOnClickListener(v -> {
                TyWebViewActivity.startActivity(mContext.get(),
                        Constant.HtmlAPI.EXCHANGE_URL);
            });

            String rankInfo = String.format(mContext.get()
                    .getString(R.string.my_rank_info), mHeader.getCount());

            SpannableString spannableString = new SpannableString(rankInfo);
            spannableString.setSpan(new JerryBoldSpan(),
                    0,
                    mHeader.getCount().length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(14, true),
                    mHeader.getCount().length() + 1,
                    rankInfo.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            headViewHolder.tvRankInfo.setText(spannableString);

        } else if (holder instanceof ContentViewHolder) {

            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;

            RankDetailResp.ScoresListBean item = mDataList.get(position - 1);

            contentViewHolder.tvContent.setText(item.getAction());
            if (item.getAmount() >= 0) {
                contentViewHolder.tvRankInfo.setText("+" + formatNum(item.getAmount()));
                contentViewHolder.tvRankInfo
                        .setTextColor(ContextCompat.getColor(mContext.get(), R.color.orange_red_color));
            } else {
                contentViewHolder.tvRankInfo.setText(formatNum(item.getAmount()));
                contentViewHolder.tvRankInfo
                        .setTextColor(ContextCompat.getColor(mContext.get(), R.color.main_text_color));
            }

            contentViewHolder.tvRankTime
                    .setText(DateUtils.getY_M_D_H_M_S_ViaTimestamp(item.getCreatedAt() * 1000L));

            if (item.getExpiredAt() < 0) {
                contentViewHolder.tvRankValidTime.setVisibility(View.GONE);
            } else {
                String expiredTime = DateUtils.getY_M_D_ViaTimestamp(item.getExpiredAt() * 1000L);
                String expiredContent = String
                        .format(mContext.get().getString(R.string.my_rank_validity_period), expiredTime);
                contentViewHolder.tvRankValidTime.setText(expiredContent);
                contentViewHolder.tvRankValidTime.setVisibility(View.VISIBLE);
            }

        } else {
            handleEmptyTip(holder, R.string.my_rank_detail_empty_tip);
        }
    }

    /**
     * 格式化数字
     */
    public String formatNum(long data) {
        return df.format(data);
    }

    public static class HeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_bg)
        ImageView ivBg;
        @BindView(R.id.tv_rank_info)
        TextView tvRankInfo;
        @BindView(R.id.tv_rank_exchange)
        TextView tvRankExchange;
        @BindView(R.id.tv_rank_order)
        TextView tvRankOrder;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_rank_info)
        TextView tvRankInfo;
        @BindView(R.id.tv_rank_time)
        TextView tvRankTime;
        @BindView(R.id.tv_rank_valid_time)
        TextView tvRankValidTime;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
