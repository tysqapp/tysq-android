package com.tysq.ty_android.feature.rank.orderRank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bit.utils.DateUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.adapter.CommonHeaderSimpleAdapter;
import com.tysq.ty_android.config.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.rank.RankOrderResp;

/**
 * author       : frog
 * time         : 2019-07-16 14:33
 * desc         : 积分订单
 * version      : 1.3.0
 */
public class OrderRankAdapter
        extends CommonHeaderSimpleAdapter<Object, RankOrderResp.ScoresorderListBean> {

    public OrderRankAdapter(Context context,
                            List<RankOrderResp.ScoresorderListBean> data) {
        super(context, null, data);
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder getContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater
                .inflate(R.layout.item_rank_order_detail, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getEmptyViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmptyViewHolder(mInflater
                .inflate(R.layout.blank_empty_tip, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;

            RankOrderResp.ScoresorderListBean item = mDataList.get(position);

            // 订单号
            holder.tvOrderNum.setText(getString(R.string.order_num, item.getOrderId()));

            // 金额数
            holder.tvOrderPriceInfo.setText("¥" + item.getPrice());

            // 积分数
            holder.tvOrderExchangeInfo.setText(item.getAmount());

            // 创建时间
            holder.tvOrderTime.setText(DateUtils.getY_M_D_H_M_S_ViaTimestamp(item.getCreatedAt() * 1000L));

            // 状态
            Integer textSourceId = Constant.ORDER_STATUS_TEXT.get(item.getStatus());
            if (textSourceId == null) {
                holder.tvOrderStatus.setVisibility(View.GONE);
            } else {
                holder.tvOrderStatus.setText(mContext.get().getString(textSourceId));
                holder.tvOrderStatus.setVisibility(View.VISIBLE);
            }

            Integer colorSourceId = Constant.ORDER_STATUS_COLOR.get(item.getStatus());
            if (colorSourceId == null) {
                holder.tvOrderStatus.setVisibility(View.GONE);
            } else {
                holder.tvOrderStatus.setTextColor(ContextCompat.getColor(mContext.get(), colorSourceId));
                holder.tvOrderStatus.setVisibility(View.VISIBLE);
            }
        } else {
            handleEmptyTip(viewHolder, R.string.my_rank_order_empty_tip);
        }

    }

    private String getString(int resourceId, String content) {
        if (mContext == null || mContext.get() == null) {
            return "";
        }
        return String.format(mContext.get().getString(resourceId), content);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.v_tag)
        View vTag;
        @BindView(R.id.tv_order_status)
        TextView tvOrderStatus;
        @BindView(R.id.tv_order_num)
        TextView tvOrderNum;
        @BindView(R.id.tv_order_price_info)
        TextView tvOrderPriceInfo;
        @BindView(R.id.tv_order_exchange_info)
        TextView tvOrderExchangeInfo;
        @BindView(R.id.tv_order_time)
        TextView tvOrderTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
