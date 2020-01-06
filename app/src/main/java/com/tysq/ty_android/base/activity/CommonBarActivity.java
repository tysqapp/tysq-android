package com.tysq.ty_android.base.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;

import butterknife.BindView;

/**
 * author       : frog
 * time         : 2019/5/16 下午2:49
 * desc         : 带返回按钮、标题、Fragment装载容器、确认按钮
 * version      : 1.3.0
 */
public abstract class CommonBarActivity extends BitBaseActivity {

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
        return R.layout.common_activity_with_bar;
    }

    @Override
    protected void initIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(v -> finish());

        // 设置标题
        tvTitle.setText(getString(getPageTitle()));
        // 添加内容
        addLayerFragment(frameLayoutContainer.getId(), getContentFragment());
    }

    protected abstract int getPageTitle();

    protected abstract Fragment getContentFragment();



}
