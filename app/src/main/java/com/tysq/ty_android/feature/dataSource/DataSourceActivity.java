package com.tysq.ty_android.feature.dataSource;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.dataSource.di.DaggerDataSourceComponent;
import com.tysq.ty_android.feature.dataSource.di.DataSourceModule;
import com.tysq.ty_android.feature.launch.UpdateDialog;
import com.tysq.ty_android.feature.main.MainActivity;
import com.tysq.ty_android.utils.DataSourceChangeUtils;

import butterknife.BindView;
import response.UpdateResp;

/**
 * author       : frog
 * time         : 2019-08-14 14:57
 * desc         : 数据源
 * version      : 1.3.0
 */

public final class DataSourceActivity
        extends BitBaseActivity<DataSourcePresenter>
        implements DataSourceView {

    @BindView(R.id.et_data_source)
    EditText etDataSource;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.iv_close)
    ImageView ivClose;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DataSourceActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_data_source;
    }

    @Override
    protected void initIntent(Intent intent) {
    }

    @Override
    protected void initView() {

        tvConfirm.setOnClickListener(v -> {
            confirm();
        });

        ivClose.setOnClickListener(v -> {
            etDataSource.setText("");
            ivClose.setVisibility(View.GONE);
        });

        etDataSource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etDataSource.getText().toString().trim().length() <= 0) {
                    ivClose.setVisibility(View.GONE);
                } else {
                    ivClose.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    /**
     * 提交数据源
     */
    private void confirm() {
        String dataSource = etDataSource.getText().toString().trim();

        if (TextUtils.isEmpty(dataSource)) {
            ToastUtils.show(getString(R.string.data_source_enter));
            return;
        }

        try {
            mPresenter.checkDataSource(dataSource);
        } catch (Exception e) {
            e.printStackTrace();
            onCheckDataSourceError();
        }
    }

    @Override
    protected void registerDagger() {
        DaggerDataSourceComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .dataSourceModule(new DataSourceModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onCheckDataSource(String dataSource) {
        ToastUtils.show(getString(R.string.data_source_success));
        mPresenter.getUpdateInfo();
    }

    @Override
    public void onCheckDataSourceError() {
        ToastUtils.show(getString(R.string.data_source_error));
    }

    @Override
    public void onGetUpdateInfo(UpdateResp updateResp) {
        if (updateResp.getUpdateType() == Constant.UpdateType.NONE) {
            goToMainActivity();
            return;
        }

        UpdateDialog fragment = UpdateDialog.newInstance();

        fragment.setVersion("V" + updateResp.getLatestVersion());

        StringBuilder featureBuilder = new StringBuilder();
        for (int i = 0; i < updateResp.getNewFeatures().size(); i++) {
            featureBuilder.append(updateResp.getNewFeatures().get(i));

            if (i < updateResp.getNewFeatures().size() - 1) {
                featureBuilder.append("\n");
            }

        }
        fragment.setVersionFeature(featureBuilder.toString());

        int updateType = updateResp.getUpdateType();
        fragment.setShowClose(updateType == Constant.UpdateType.ORDINARY);

        fragment.show(this);
    }

    @Override
    public void onGetUpdateInfoError() {
        goToMainActivity();
    }

    @Override
    public void onCheckDataSourceInvalidate() {
        ToastUtils.show(getString(R.string.data_source_please_enter_correct));
    }

    public void goToMainActivity() {
        DataSourceChangeUtils.initHtml();

        MainActivity.startActivity(this);
        finish();
    }
}
