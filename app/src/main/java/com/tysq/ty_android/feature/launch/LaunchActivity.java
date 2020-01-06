package com.tysq.ty_android.feature.launch;

import android.content.Context;
import android.content.Intent;

import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.launch.di.DaggerLaunchComponent;
import com.tysq.ty_android.feature.launch.di.LaunchModule;
import com.tysq.ty_android.feature.main.MainActivity;

import java.lang.Override;

import response.UpdateResp;

/**
 * author       : frog
 * time         : 2019-08-22 14:34
 * desc         : 启动页
 * version      : 1.3.0
 */

public final class LaunchActivity
        extends BitBaseActivity<LaunchPresenter>
        implements LaunchView {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LaunchActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        showDialog();
        mPresenter.getUpdateInfo();
    }

    @Override
    protected void registerDagger() {
        DaggerLaunchComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .launchModule(new LaunchModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onGetUpdateInfo(UpdateResp value) {
        if (value.getUpdateType() == Constant.UpdateType.NONE) {
            goToMainActivity();
            return;
        }

        UpdateDialog fragment = UpdateDialog.newInstance();

        fragment.setVersion("V" + value.getLatestVersion());

        StringBuilder featureBuilder = new StringBuilder();
        for (int i = 0; i < value.getNewFeatures().size(); i++) {
            featureBuilder.append(value.getNewFeatures().get(i));

            if (i < value.getNewFeatures().size() - 1) {
                featureBuilder.append("\n");
            }

        }
        fragment.setVersionFeature(featureBuilder.toString());

        int updateType = value.getUpdateType();
        fragment.setShowClose(updateType == Constant.UpdateType.ORDINARY);
        fragment.show(this);
    }

    @Override
    public void onGetUpdateInfoError() {
        goToMainActivity();
    }

    public void goToMainActivity() {
        hideDialog();
        MainActivity.startActivity(this);
        finish();
    }

}
