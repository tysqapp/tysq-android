package com.tysq.ty_android.base.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;

import butterknife.BindView;

/**
 * author       : liaozhenlin
 * time         : 2019/5/16 下午2:49
 * desc         : 带返回按钮、标题、Fragment装载容器、确认按钮，可重写确认按钮的点击方法、改变样式和字体颜色
 * version      : 1.4.0
 */
public abstract class CommonBarStrengthenActivity extends BitBaseActivity {

    @BindView(R.id.iv_back)
    public ImageView ivBack;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.frame_layout_container)
    public FrameLayout frameLayoutContainer;
    @BindView(R.id.tv_confirm)
    public TextView tvConfirm;

    @Override
    protected int getLayout() {
        return R.layout.common_activity_with_bar_strengthen;
    }

    @Override
    protected void initIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(v -> finish());

        //设置按钮
        if (isGetConfirm()){
            tvConfirm.setVisibility(View.VISIBLE);
        }

        //设置按钮文本
        if (getConfirmText() != 0 ){
            tvConfirm.setText(getConfirmText());
        }
        //设置按钮背景
        if (getConfirmBackground() != 0){
            tvConfirm.setBackgroundResource(getConfirmBackground());
        }

        //设置按钮的字体颜色
        if (getConfirmTextColor() != 0){
            tvConfirm.setTextColor(this.getResources().getColor(getConfirmTextColor()));
        }


        // 设置标题
        tvTitle.setText(getString(getPageTitle()));
        // 添加内容
        addLayerFragment(frameLayoutContainer.getId(), getContentFragment());
    }

    protected abstract int getPageTitle();

    protected abstract Fragment getContentFragment();

    //是否显示确认按钮
    protected abstract boolean isGetConfirm();

    //改变确认按钮的文本
    protected abstract int getConfirmText();

    //改变确认按钮的背景
    protected abstract int getConfirmBackground();

    //改变确认按钮字体颜色
    protected abstract int getConfirmTextColor();



}
