package com.tysq.ty_android.feature.aboutUs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.BuildConfig;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.activity.CommonToolbarActivity;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.aboutUs.di.AboutUsModule;
import com.tysq.ty_android.feature.aboutUs.di.DaggerAboutUsComponent;
import com.tysq.ty_android.feature.launch.UpdateDialog;

import butterknife.BindView;
import response.UpdateResp;

/**
 * author       : frog
 * time         : 2019-07-29 16:14
 * desc         : 关于我们
 * version      : 1.3.0
 */
public final class AboutUsFragment
        extends BitBaseFragment<AboutUsPresenter>
        implements AboutUsView, CommonToolbarActivity.ICommonFragment {

    public static final String TAG = "AboutUsFragment";

    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.ll_update)
    LinearLayout llUpdate;

    public static AboutUsFragment newInstance() {

        Bundle args = new Bundle();

        AboutUsFragment fragment = new AboutUsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    protected void registerDagger() {
        DaggerAboutUsComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .aboutUsModule(new AboutUsModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initView(View view) {
        tvCode.setText(
                String.format(getString(R.string.about_us_code_info), BuildConfig.VERSION_NAME));

        llUpdate.setOnClickListener(v -> {
            mPresenter.getUpdateInfo();
        });
    }

    @Override
    public int getTitleId() {
        return R.string.about_us;
    }

    @Override
    public void onGetUpdateInfo(UpdateResp updateResp) {
        if (updateResp.getUpdateType() == Constant.UpdateType.NONE) {
            ToastUtils.show(getString(R.string.update_newest));
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
    }
}