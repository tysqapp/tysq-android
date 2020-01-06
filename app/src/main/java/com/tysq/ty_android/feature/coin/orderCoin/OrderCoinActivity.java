package com.tysq.ty_android.feature.coin.orderCoin;

import android.content.Context;
import android.content.Intent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.feature.coin.orderCoin.tip.CoinOrderTipDialog;
import com.tysq.ty_android.feature.rank.RankOrderTipDialog;
import com.tysq.ty_android.feature.rank.orderRank.OrderRankFragment;

import butterknife.BindView;
/**
 * author       : liaozhenlin
 * time         : 2019/11/13 16:43
 * desc         : 金币订单
 * version      : 1.5.0
 */
public class OrderCoinActivity extends BitBaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.frame_layout_container)
    FrameLayout frameLayoutContainer;
    @BindView(R.id.tv_des)
    TextView tvDes;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, OrderCoinActivity.class);

        context.startActivity(intent);
    }


    @Override
    protected int getLayout() { return R.layout.activity_order_rank; }

    @Override
    protected void initIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(v -> finish());

        OrderCoinFragment fragment = OrderCoinFragment.newInstance();

        // 设置标题
        tvTitle.setText(getString(fragment.getTitleId()));
        // 添加内容
        addLayerFragment(frameLayoutContainer.getId(), fragment);

        tvDes.setOnClickListener(v -> CoinOrderTipDialog.newInstance().show(this));
    }
}
