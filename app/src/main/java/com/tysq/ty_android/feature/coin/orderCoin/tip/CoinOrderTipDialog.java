package com.tysq.ty_android.feature.coin.orderCoin.tip;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;

/**
 * author       : liaozhenlin
 * time         : 2019/11/12 17:53
 * desc         : 金币弹窗
 * version      : 1.5.0
 */
public class CoinOrderTipDialog extends CommonBaseDialog {

    private ImageView ivClose;

    public static CoinOrderTipDialog newInstance() {

        Bundle args = new Bundle();

        CoinOrderTipDialog fragment = new CoinOrderTipDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_coin_order_des;
    }

    @Override
    protected int obtainWidth() {
        return dp2px(300);
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
    protected void initArgs(Bundle arguments) {

    }

    @Override
    protected void initView(View view) {
        ivClose = view.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(v -> {
            dismiss();
        });
    }
}
