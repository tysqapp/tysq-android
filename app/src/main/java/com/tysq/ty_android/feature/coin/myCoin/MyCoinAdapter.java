package com.tysq.ty_android.feature.coin.myCoin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.utils.DateUtils;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.activity.CommonToolbarActivity;
import com.tysq.ty_android.base.adapter.CommonHeaderSimpleAdapter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.coin.coinWithdraw.CoinWithdrawActivity;
import com.tysq.ty_android.feature.coin.coinWithdrawLog.CoinWithdrawLogFragment;
import com.tysq.ty_android.feature.coin.orderCoin.OrderCoinActivity;
import com.tysq.ty_android.feature.emailVerify.EmailVerifyActivity;
import com.tysq.ty_android.feature.web.TyWebViewActivity;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.utils.TyUtils;
import com.zinc.lib_jerry_editor.span.typeface.JerryBoldSpan;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import response.coin.MyCoinDetailResp;
import response.coin.MyCoinResp;

/**
 * author       : frog
 * time         : 2019-07-15 18:24
 * desc         : 我的金币
 * version      : 1.3.0
 */
public class MyCoinAdapter extends CommonHeaderSimpleAdapter<MyCoinResp, MyCoinDetailResp.DetailsInfoBean> {

    private final DecimalFormat df = new DecimalFormat("#,###");

    public MyCoinAdapter(Context context,
                         MyCoinResp myCoinResp,
                         List<MyCoinDetailResp.DetailsInfoBean> detailData) {
        super(context, myCoinResp, detailData);
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HeadViewHolder(mInflater
                .inflate(R.layout.item_my_coin_title, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(mInflater
                .inflate(R.layout.item_my_coin_detail, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getEmptyViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmptyViewHolder(mInflater
                .inflate(R.layout.blank_empty_half_tip, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {

            HeadViewHolder headViewHolder = (HeadViewHolder) holder;

            String coinInfo = String.format(mContext.get()
                    .getString(R.string.my_coin_info), mHeader.getCount());

            SpannableString spannableString = new SpannableString(coinInfo);
            spannableString.setSpan(new JerryBoldSpan(),
                    0,
                    mHeader.getCount().length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(14, true),
                    mHeader.getCount().length() + 1,
                    coinInfo.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            headViewHolder.tvCoinInfo.setText(spannableString);

            if (mHeader.getCountNum() <= 0){
                headViewHolder.tvCoinFormat.setVisibility(View.GONE);
            }else {
                headViewHolder.tvCoinFormat.setVisibility(View.VISIBLE);
                String coinFormat = String.format(mContext.get()
                        .getString(R.string.mine_coin_format), TyUtils.formatCoin(mHeader.getCountNum()));
                headViewHolder.tvCoinFormat.setText(coinFormat);
            }

            headViewHolder.tvCoinWithdraw.setOnClickListener(
                    v -> CoinWithdrawActivity.startActivity(mContext.get(), mHeader.getCountNum())
            );

            headViewHolder.tvCoinBuy.setOnClickListener(
                    v -> TyWebViewActivity.startActivity(mContext.get(), Constant.HtmlAPI.COIN_URL)
            );

        } else if (holder instanceof ContentViewHolder) {

            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;

            MyCoinDetailResp.DetailsInfoBean item = mDataList.get(position - 1);

            contentViewHolder.tvContent.setText(item.getAction());
            if (item.getChangeNumber() >= 0) {
                contentViewHolder.tvCoinInfo.setText("+" + formatNum(item.getChangeNumber()));
                contentViewHolder.tvCoinInfo
                        .setTextColor(ContextCompat.getColor(mContext.get(), R.color.orange_red_color));
            } else {
                contentViewHolder.tvCoinInfo.setText(formatNum(item.getChangeNumber()));
                contentViewHolder.tvCoinInfo
                        .setTextColor(ContextCompat.getColor(mContext.get(), R.color.main_text_color));
            }

            contentViewHolder.tvCoinTime
                    .setText(DateUtils.getY_M_D_H_M_S_ViaTimestamp(item.getTime() * 1000L));

        } else {
            handleEmptyTip(holder, R.string.my_coin_detail_empty_tip);
        }
    }

    /**
     * 格式化数字
     */
    public String formatNum(long data) {
        return df.format(data);
    }

    public static class HeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_coin_info)
        TextView tvCoinInfo;
        @BindView(R.id.tv_coin_withdraw)
        TextView tvCoinWithdraw;
        @BindView(R.id.tv_coin_format)
        TextView tvCoinFormat;
        @BindView(R.id.tv_coin_buy)
        TextView tvCoinBuy;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_coin_info)
        TextView tvCoinInfo;
        @BindView(R.id.tv_coin_time)
        TextView tvCoinTime;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
