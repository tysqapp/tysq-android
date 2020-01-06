package com.tysq.ty_android.feature.coin.orderCoin;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
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
import response.coin.CoinOrderResp;

public class OrderCoinAdapter
        extends CommonHeaderSimpleAdapter<Object, CoinOrderResp.CoinOrdersBean> {

    public OrderCoinAdapter(Context context,
                            List<CoinOrderResp.CoinOrdersBean> coinOrdersBeans) {
        super(context,null,coinOrdersBeans);
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder getContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                mInflater.inflate(R.layout.item_coin_order_detail, parent, false)
        );
    }

    @Override
    protected RecyclerView.ViewHolder getEmptyViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmptyViewHolder(mInflater
                .inflate(R.layout.blank_empty_tip, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;

            CoinOrderResp.CoinOrdersBean item = mDataList.get(position);

            //订单号
            viewHolder.tvOrderNum.setText(mContext.get().getString(R.string.order_num, item.getOrderId()));

            //金额数
            viewHolder.tvOrderPriceInfo.setText("¥" + item.getPrice());

            //购买金币数
            viewHolder.tvOrderExchangeInfo.setText(String.valueOf(item.getCoinAmount()));

            //创建时间
            viewHolder.tvOrderTime.setText(DateUtils.getY_M_D_H_M_S_ViaTimestamp(item.getCreateAt() * 1000L));

            //状态
            if (item.getStatus() == 0){
                viewHolder.tvOrderStatus.setText(mContext.get().getString(R.string.order_wait_for_pay));
                viewHolder.tvOrderStatus.setTextColor(mContext.get().getResources().getColor(R.color.main_blue_color));
            } else if (item.getStatus() == 1){
                viewHolder.tvOrderStatus.setText(mContext.get().getString(R.string.my_coin_order_paid_already));
                viewHolder.tvOrderStatus.setTextColor(mContext.get().getResources().getColor(R.color.green_color));
            }
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_order_num)
        TextView tvOrderNum;
        @BindView(R.id.tv_order_status)
        TextView tvOrderStatus;
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
