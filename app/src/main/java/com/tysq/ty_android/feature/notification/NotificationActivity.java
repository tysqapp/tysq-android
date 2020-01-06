package com.tysq.ty_android.feature.notification;

import android.content.Context;
import android.content.Intent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.base.activity.CommonToolbarStrengthenActivity;
import com.tysq.ty_android.feature.notificationSetting.NotificationSettingFragment;

import butterknife.BindView;

/**
 * author       : frog
 * time         : 2019-08-29 17:30
 * desc         : 通知
 * version      : 1.0.0
 */
public class NotificationActivity extends BitBaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.frame_layout_container)
    FrameLayout frameLayoutContainer;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, NotificationActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_notification;
    }

    @Override
    protected void initIntent(Intent intent) {

    }

    @Override
    protected void initView() {

        ivBack.setOnClickListener(v -> {
            finish();
        });

        tvSetting.setOnClickListener(v -> {
            CommonToolbarStrengthenActivity.startActivity(NotificationActivity.this,
                    NotificationSettingFragment.TAG);
        });

        addLayerFragment(frameLayoutContainer.getId(), NotificationFragment.newInstance());
    }


}
