package com.tysq.ty_android.feature.dataSourceSetting;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.activity.CommonBarStrengthenActivity;
import com.tysq.ty_android.feature.DoorActivity;
import com.tysq.ty_android.utils.DataSourceChangeUtils;
import com.tysq.ty_android.utils.TyUtils;

import butterknife.BindView;

/**
 * author       : frog
 * time         : 2019-07-15 17:46
 * desc         : 设置数据源
 * version      : 1.3.0
 */
public class DataSourceSettingActivity extends CommonBarStrengthenActivity {

    private static final String TYPE = "type";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.frame_layout_container)
    FrameLayout frameLayoutContainer;

    private DataSourceSettingFragment fragment;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DataSourceSettingActivity.class);


        context.startActivity(intent);
    }

    @Override
    protected void initIntent(Intent intent) {
    }

    @Override
    protected void initView() {
        super.initView();

        tvConfirm.setOnClickListener(v -> {
            fragment.isCommit();
        });

    }

    @Override
    protected int getPageTitle() {
        return R.string.data_source_setting;
    }

    @Override
    protected Fragment getContentFragment() {
        fragment = DataSourceSettingFragment.newInstance();
        return fragment;
    }

    @Override
    protected boolean isGetConfirm() {
        return true;
    }

    @Override
    protected int getConfirmText() {
        return 0;
    }

    @Override
    protected int getConfirmBackground() {
        return R.drawable.selector_btn_round_blue_bg;
    }

    @Override
    protected int getConfirmTextColor() {
        return R.color.white;
    }

    public void closeAll() {
        exit(this);

        DataSourceChangeUtils.cleanCache();
        DataSourceChangeUtils.cleanFragment();
        DataSourceChangeUtils.cleanNet();

        DoorActivity.startActivity(this);
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //隐藏软键盘
        TyUtils.hideKeyBoard(ev, this);
        return super.dispatchTouchEvent(ev);
    }

}
