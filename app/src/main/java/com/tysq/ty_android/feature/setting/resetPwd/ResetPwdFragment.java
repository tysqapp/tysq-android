package com.tysq.ty_android.feature.setting.resetPwd;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.adapter.TextWatcherAdapter;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.setting.resetPwd.di.DaggerResetPwdComponent;
import com.tysq.ty_android.feature.setting.resetPwd.di.ResetPwdModule;

import butterknife.BindView;

public final class ResetPwdFragment extends BitBaseFragment<ResetPwdPresenter>
        implements ResetPwdView, View.OnClickListener {

    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.iv_old_pwd)
    ImageView ivOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.iv_new_pwd)
    ImageView ivNewPwd;
    @BindView(R.id.et_new_pwd_confirm)
    EditText etNewPwdConfirm;
    @BindView(R.id.iv_new_pwd_confirm)
    ImageView ivNewPwdConfirm;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    private Drawable showPwdDrawable;
    private Drawable hidePwdDrawable;

    private boolean isHidePwdForOld;
    private boolean isHidePwdForNew1;
    private boolean isHidePwdForNew2;

    public static ResetPwdFragment newInstance() {
        Bundle args = new Bundle();

        ResetPwdFragment fragment = new ResetPwdFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container,
                                        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reset_pwd, container, false);
    }

    @Override
    protected void initView(View view) {

        isHidePwdForOld = true;
        isHidePwdForNew1 = true;
        isHidePwdForNew2 = true;

        showPwdDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_pwd_show);
        hidePwdDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_pwd_hide);

        tvConfirm.setEnabled(false);

        ivOldPwd.setOnClickListener(this);
        ivNewPwd.setOnClickListener(this);
        ivNewPwdConfirm.setOnClickListener(this);

        tvConfirm.setOnClickListener(this);

        TextWatcherAdapter textWatcherAdapter = new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                checkPwdState();
            }
        };

        etOldPwd.addTextChangedListener(textWatcherAdapter);
        etNewPwd.addTextChangedListener(textWatcherAdapter);
        etNewPwdConfirm.addTextChangedListener(textWatcherAdapter);

    }

    private void checkPwdState() {
        String oldPwd = etOldPwd.getText().toString().trim();
        String newPwd1 = etNewPwd.getText().toString().trim();
        String newPwd2 = etNewPwdConfirm.getText().toString().trim();

        boolean result = true;
        if (TextUtils.isEmpty(oldPwd)) {
            result = false;
        } else if (TextUtils.isEmpty(newPwd1) || newPwd1.length() < 6) {
            result = false;
        } else if (TextUtils.isEmpty(newPwd2) || newPwd2.length() < 6) {
            result = false;
        }

        tvConfirm.setEnabled(result);
    }

    @Override
    protected void registerDagger() {
        DaggerResetPwdComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .resetPwdModule(new ResetPwdModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_old_pwd:
                isHidePwdForOld = !isHidePwdForOld;
                setPwdState(ivOldPwd, etOldPwd, isHidePwdForOld);
                break;
            case R.id.iv_new_pwd:
                isHidePwdForNew1 = !isHidePwdForNew1;
                setPwdState(ivNewPwd, etNewPwd, isHidePwdForNew1);
                break;
            case R.id.iv_new_pwd_confirm:
                isHidePwdForNew2 = !isHidePwdForNew2;
                setPwdState(ivNewPwdConfirm, etNewPwdConfirm, isHidePwdForNew2);
                break;
            case R.id.tv_confirm:
                validPwd();
                break;
        }
    }

    private void validPwd() {
        String oldPwd = etOldPwd.getText().toString().trim();
        String newPwd1 = etNewPwd.getText().toString().trim();
        String newPwd2 = etNewPwdConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(oldPwd)) {
            ToastUtils.show(getString(R.string.reset_pwd_enter_old));
            return;
        }

        if (TextUtils.isEmpty(newPwd1) || newPwd1.length() < 6) {
            ToastUtils.show(getString(R.string.reset_pwd_enter_new));
            return;
        }

        if (TextUtils.isEmpty(newPwd2) || newPwd2.length() < 6) {
            ToastUtils.show(getString(R.string.reset_pwd_enter_new));
            return;
        }

        if (!newPwd1.equals(newPwd2)) {
            ToastUtils.show(getString(R.string.reset_pwd_enter_same));
            return;
        }

        showDialog();
        mPresenter.resetPwd(oldPwd, newPwd1);
    }

    private void setPwdState(ImageView imageView,
                             EditText editText,
                             boolean isHidePwd) {
        int start = editText.getSelectionStart();

        if (isHidePwd) {
            imageView.setImageDrawable(hidePwdDrawable);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            imageView.setImageDrawable(showPwdDrawable);
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }

        editText.setSelection(start);
    }

    @Override
    public void onResetPwd() {
        if (getActivity() != null) {
            getActivity().finish();
        }
        hideDialog();
        ToastUtils.show(getString(R.string.reset_pwd_success));
    }
}
