package com.tysq.ty_android.feature.rank;

import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;

/**
 * author       : frog
 * time         : 2019-08-12 16:49
 * desc         : 积分订单弹框
 * version      : 1.0.0
 */
public class RankOrderTipDialog extends CommonBaseDialog {

    private ImageView ivClose;

    public static RankOrderTipDialog newInstance() {

        Bundle args = new Bundle();

        RankOrderTipDialog fragment = new RankOrderTipDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_order_des;
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
    protected void initView(View view) {
        ivClose = view.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(v -> {
            dismiss();
        });

    }

}
