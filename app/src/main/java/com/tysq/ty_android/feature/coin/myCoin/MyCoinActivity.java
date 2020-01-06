package com.tysq.ty_android.feature.coin.myCoin;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bit.utils.UIUtils;
import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.activity.CommonToolbarActivity;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.coin.coinWithdrawLog.CoinWithdrawLogFragment;
import com.tysq.ty_android.feature.coin.orderCoin.OrderCoinActivity;
import com.tysq.ty_android.feature.web.TyWebViewActivity;

import butterknife.BindView;

import static com.tysq.ty_android.app.TyApplication.getContext;

/**
 * author       : liaozhenlin
 * time         : 2019/11/5 14:12
 * desc         : 我的金币
 * version      : 1.5.0
 */
public class MyCoinActivity extends BitBaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.frame_layout_container)
    FrameLayout frameLayoutContainer;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;

    private PopupWindow mTypeWindow;
    private int yOffset;
    private int xOffset;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyCoinActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_my_coin;
    }

    @Override
    protected void initIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(v -> finish());

        initPopupWindow();

        xOffset = UIUtils.dip2px(this, 45);
        yOffset = UIUtils.px2dip(this, 45);

        MyCoinFragment fragment = MyCoinFragment.newInstance();

        // 设置标题
        tvTitle.setText(getString(fragment.getTitleId()));
        // 添加内容
        addLayerFragment(frameLayoutContainer.getId(), fragment);

        ivMenu.setOnClickListener(v -> showPopupWindow());
    }



    //弹窗
    private void initPopupWindow(){
        View window = getLayoutInflater().inflate(R.layout.activity_my_coin_popupwindow, null);

        window.findViewById(R.id.tv_coin_des).setOnClickListener(this);
        window.findViewById(R.id.tv_withdraw_log).setOnClickListener(this);
        window.findViewById(R.id.tv_coin_order).setOnClickListener(this);

        mTypeWindow = new PopupWindow(window,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        mTypeWindow.setOutsideTouchable(true);
        mTypeWindow.setBackgroundDrawable(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_one_inch_alpha));
    }

    private void showPopupWindow() {
        mTypeWindow.showAsDropDown(ivMenu,-xOffset,-yOffset,Gravity.START);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_coin_des:
                TyWebViewActivity.startActivity(this, Constant.HtmlAPI.COIN);
                dismiss();
                break;
            case R.id.tv_withdraw_log:
                CommonToolbarActivity.startActivity(this, CoinWithdrawLogFragment.TAG);
                dismiss();
                break;
            case R.id.tv_coin_order:
                OrderCoinActivity.startActivity(this);
                dismiss();
                break;
        }
    }

    private void dismiss(){
        mTypeWindow.dismiss();
    }
}
