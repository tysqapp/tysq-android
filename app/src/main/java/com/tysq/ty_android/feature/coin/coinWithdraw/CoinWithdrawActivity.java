package com.tysq.ty_android.feature.coin.coinWithdraw;

import android.content.Context;
import android.content.Intent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;

import butterknife.BindView;

/**
 * author       : frog
 * time         : 2019-07-15 17:46
 * desc         : 提现
 * version      : 1.3.0
 */
public class CoinWithdrawActivity extends BitBaseActivity {

    private static final String COIN_AMOUNT = "coin_amount";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.frame_layout_container)
    FrameLayout frameLayoutContainer;

    // 金币量
    private long coinAmount;

    public static void startActivity(Context context, long coinAmount) {
        Intent intent = new Intent(context, CoinWithdrawActivity.class);

        intent.putExtra(COIN_AMOUNT, coinAmount);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_common_toolbar;
    }

    @Override
    protected void initIntent(Intent intent) {
        coinAmount = intent.getLongExtra(COIN_AMOUNT, 0);
    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(v -> finish());

        // 设置标题
        tvTitle.setText(getString(R.string.withdraw));
        // 添加内容
        addLayerFragment(frameLayoutContainer.getId(), CoinWithdrawFragment.newInstance(coinAmount));

    }

}
