package com.tysq.ty_android.feature.coin.coinWithdrawLog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.utils.DateUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.adapter.CommonSimpleAdapter;
import com.tysq.ty_android.config.Constant;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.coin.WithdrawLogResp;

/**
 * author       : frog
 * time         : 2019-08-13 11:36
 * desc         : 金币提现
 * version      : 1.3.0
 */
public class CoinWithdrawLogAdapter
        extends CommonSimpleAdapter<WithdrawLogResp.WithdrawReviewListBean,
        CoinWithdrawLogAdapter.ContentViewHolder> {

    private static final DecimalFormat DF = new DecimalFormat("0.########");

    public CoinWithdrawLogAdapter(Context context,
                                  List<WithdrawLogResp.WithdrawReviewListBean> withdrawReviewListBeans) {
        super(context, withdrawReviewListBeans);
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(
                mInflater.inflate(R.layout.item_coin_withdraw_detail, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        WithdrawLogResp.WithdrawReviewListBean data = mDataList.get(position);

        String id = mContext.get().getString(R.string.withdraw_id);
        holder.tvId.setText(String.format(id, data.getId()));

        holder.tvCoinCountInfo.setText(data.getCoinAmount());

        holder.tvBtcCountInfo.setText(DF.format(data.getBtcAmount()));

        holder.tvAddressInfo.setText(data.getAddress());

        if (TextUtils.isEmpty(data.getNote())) {
            holder.llRemark.setVisibility(View.GONE);
        } else {
            holder.llRemark.setVisibility(View.VISIBLE);
            holder.tvRemarkInfo.setText(data.getNote());
        }

        holder.tvApplyTimeInfo.setText(DateUtils.getY_M_D_H_M_S_ViaTimestamp(data.getCreatedAt() * 1000L));

        holder.tvStatus.setText(Constant.WITHDRAW_STATUS_TEXT.get(data.getStatus()));
        int stateTextColor = Constant.WITHDRAW_STATUS_COLOR.get(data.getStatus());
        holder.tvStatus.setTextColor(ContextCompat.getColor(mContext.get(), stateTextColor));

        if (TextUtils.isEmpty(data.getReason())) {
            holder.llRefuseReason.setVisibility(View.GONE);
        } else {
            holder.llRefuseReason.setVisibility(View.VISIBLE);
            holder.tvReasonInfo.setText(data.getReason());
        }

    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.v_divider_1)
        View vDivider1;
        @BindView(R.id.v_tag)
        View vTag;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_id)
        TextView tvId;
        @BindView(R.id.v_divider_2)
        View vDivider2;
        @BindView(R.id.tv_coin_count)
        TextView tvCoinCount;
        @BindView(R.id.tv_coin_count_info)
        TextView tvCoinCountInfo;
        @BindView(R.id.tv_btc_count)
        TextView tvBtcCount;
        @BindView(R.id.tv_btc_count_info)
        TextView tvBtcCountInfo;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_address_info)
        TextView tvAddressInfo;
        @BindView(R.id.tv_remark)
        TextView tvRemark;
        @BindView(R.id.tv_remark_info)
        TextView tvRemarkInfo;
        @BindView(R.id.tv_apply_time)
        TextView tvApplyTime;
        @BindView(R.id.tv_apply_time_info)
        TextView tvApplyTimeInfo;
        @BindView(R.id.tv_reason)
        TextView tvReason;
        @BindView(R.id.tv_reason_info)
        TextView tvReasonInfo;

        @BindView(R.id.ll_remark)
        LinearLayout llRemark;
        @BindView(R.id.ll_refuse_reason)
        LinearLayout llRefuseReason;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
