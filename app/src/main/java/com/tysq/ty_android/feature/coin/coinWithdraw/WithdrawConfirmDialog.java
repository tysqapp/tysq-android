package com.tysq.ty_android.feature.coin.coinWithdraw;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;
import com.tysq.ty_android.utils.TyUtils;

/**
 * author       : frog
 * time         : 2019-08-15 14:50
 * desc         : 提现确认弹框
 * version      : 1.3.0
 */

public class WithdrawConfirmDialog
        extends CommonBaseDialog {

    private WithdrawListener mListener;

    private TextView tvAmountInfo;
    private TextView tvExchangeBtcInfo;
    private TextView tvFeeInfo;
    private TextView tvResultInfo;
    private TextView tvCommit;
    private ImageView ivClose;

    private long amount;
    private String btc;
    private String fee;
    private String result;

    public static WithdrawConfirmDialog newInstance() {

        Bundle args = new Bundle();

        WithdrawConfirmDialog fragment = new WithdrawConfirmDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_withdraw_tip;
    }

    @Override
    protected int obtainWidth() {
        return dp2px(260);
    }

    @Override
    protected int obtainHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected int obtainGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void initView(View view) {

        tvAmountInfo = view.findViewById(R.id.tv_amount_info);

        tvExchangeBtcInfo = view.findViewById(R.id.tv_exchange_btc_info);

        tvFeeInfo = view.findViewById(R.id.tv_fee_info);

        tvResultInfo = view.findViewById(R.id.tv_result_info);
        tvCommit = view.findViewById(R.id.tv_commit);
        ivClose = view.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(v -> {
            dismiss();
        });

        tvCommit.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onConfirmCommit();
            }
            dismiss();
        });

        setInfo();

    }

    /**
     * 设置信息
     *
     * @param amount
     * @param btc
     * @param fee
     * @param result
     */
    public void setInfo(long amount,
                        String btc,
                        String fee,
                        String result) {
        this.amount = amount;
        this.btc = btc;
        this.fee = fee;
        this.result = result;
    }

    private void setInfo() {
        tvAmountInfo.setText(TyUtils.formatDotNum(amount));

        tvExchangeBtcInfo.setText(
                String.format(getString(R.string.withdraw_btc), btc));

        tvFeeInfo.setText(String.format(getString(R.string.withdraw_btc_amount), fee));

        tvResultInfo.setText(String.format(getString(R.string.withdraw_btc_amount), result));
    }

    public void setListener(WithdrawListener listener) {
        this.mListener = listener;
    }

    @Override
    public void dismiss() {
        mListener = null;
        super.dismiss();
    }

    public interface WithdrawListener {

        /**
         * 确认提交
         */
        void onConfirmCommit();

    }

}
