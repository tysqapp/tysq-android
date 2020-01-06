package com.tysq.ty_android.feature.launch;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.CommonBaseDialog;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.dataSource.DataSourceActivity;
import com.tysq.ty_android.utils.TyUtils;

/**
 * author       : frog
 * time         : 2019-08-22 15:17
 * desc         : 升级提示框
 * version      : 1.0.0
 */
public class UpdateDialog extends CommonBaseDialog {

    private ImageView ivUpdateTitle;
    private TextView tvVersion;
    private TextView tvVersionInfo;
    private TextView tvFeature;
    private TextView tvFeatureInfo;
    private TextView tvUpdate;
    private ImageView ivClose;

    private boolean isShowClose = false;
    private String mVersionFeature;
    private String mVersion;

    public static UpdateDialog newInstance() {

        Bundle args = new Bundle();

        UpdateDialog fragment = new UpdateDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setShowClose(boolean showClose) {
        isShowClose = showClose;
    }

    public void setVersionFeature(String versionFeature) {
        this.mVersionFeature = versionFeature;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    @Override
    protected void initArgs(Bundle arguments) {
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_update_info;
    }

    @Override
    protected int obtainWidth() {
        return dp2px(255);
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

        setCancelable(false);

        ivUpdateTitle = view.findViewById(R.id.iv_update_title);
        tvVersion = view.findViewById(R.id.tv_version);
        tvVersionInfo = view.findViewById(R.id.tv_version_info);
        tvFeature = view.findViewById(R.id.tv_feature);
        tvFeatureInfo = view.findViewById(R.id.tv_feature_info);
        tvUpdate = view.findViewById(R.id.tv_update);
        ivClose = view.findViewById(R.id.iv_close);

        if (isShowClose) {
            ivClose.setVisibility(View.VISIBLE);
        } else {
            ivClose.setVisibility(View.GONE);
        }

        ivClose.findViewById(R.id.iv_close).setOnClickListener(v -> {
            dismiss();
            if (getActivity() == null) {
                return;
            }

            if (getActivity() instanceof LaunchActivity) {
                ((LaunchActivity) getActivity()).goToMainActivity();
            } else if (getActivity() instanceof DataSourceActivity) {
                ((DataSourceActivity) getActivity()).goToMainActivity();
            }
        });

        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TyUtils.goToOuterBrowser(Constant.HtmlAPI.DOWNLOAD);
            }
        });

        tvVersionInfo.setText(mVersion);
        tvFeatureInfo.setText(mVersionFeature);
    }

}
