package com.tysq.ty_android.feature.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.emailVerify.EmailVerifyActivity;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.utils.DataSourceChangeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import eventbus.LoginStateChangeEvent;
import eventbus.NotificationUpdateEvent;
import eventbus.VerifyEmailEvent;

/**
 * author       : frog
 * time         : 2019/5/16 下午2:59
 * desc         :
 * version      :
 */
public class SettingFragment extends BitBaseFragment
        implements View.OnClickListener {

    @BindView(R.id.ll_account)
    LinearLayout llAccount;

    @BindView(R.id.ll_password)
    LinearLayout llPassword;

    @BindView(R.id.tv_quit)
    TextView tvQuit;

    @BindView(R.id.tv_account)
    TextView tvAccount;

    @BindView(R.id.tv_email_verify)
    TextView tvEmailVerify;

    public static SettingFragment newInstance() {
        Bundle args = new Bundle();

        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    protected void initView(View view) {

        tvAccount.setText(UserCache.getDefault().getEmail());

        llPassword.setOnClickListener(this);
        tvQuit.setOnClickListener(this);
        tvEmailVerify.setOnClickListener(this);

        verifyBtnVisibleControl();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_password:
                SettingActivity.startActivity(getContext(), SettingActivity.RESET);
                break;

            case R.id.tv_quit:

                new MaterialDialog.Builder(getContext())
                        .title(getString(R.string.setting_quit_tip))
                        .negativeColor(ContextCompat.getColor(getContext(), R.color.et_tip_text_color))
                        .negativeText(getString(R.string.setting_quit_sure))
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                handleQuit();
                            }
                        })
                        .positiveColor(ContextCompat.getColor(getContext(), R.color.main_blue_color))
                        .positiveText(getString(R.string.setting_quit_cancel))
                        .show();

                break;

            case R.id.tv_email_verify:
                EmailVerifyActivity.startActivity(getContext(), UserCache.getDefault().getEmail());
                break;
        }
    }

    private void handleQuit() {

        DataSourceChangeUtils.cleanCache();

        EventBus.getDefault().post(new LoginStateChangeEvent());

        EventBus.getDefault().post(new NotificationUpdateEvent(0));

        if (getActivity() != null) {
            getActivity().finish();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshVerifyBtn(VerifyEmailEvent event) {
        verifyBtnVisibleControl();
    }

    private void verifyBtnVisibleControl() {
        if (UserCache.getDefault().getEmailStatus() == Constant.EmailValidateStatus.EMAIL_VERIFY_SUC) {
            tvEmailVerify.setVisibility(View.GONE);
        } else {
            tvEmailVerify.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getInitRegister() {
        return BUTTER_KNIFE | EVENT_BUS;
    }
}
