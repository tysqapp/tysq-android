package com.tysq.ty_android.feature.person.personResumeChange;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.person.personResumeChange.di.DaggerPersonResumeChangeComponent;
import com.tysq.ty_android.feature.person.personResumeChange.di.PersonResumeChangeModule;
import com.tysq.ty_android.local.sp.UserCache;

import butterknife.BindView;

/**
 * author       : liaozhenlin
 * time         : 2019/9/11 17:01
 * desc         : 更新个人简介
 * version      : 1.0.0
 */
public final class PersonResumeChangeFragment extends
        BitBaseFragment<PersonResumeChangePresenter>
        implements PersonResumeChangeView, TextWatcher {

    @BindView(R.id.et_resume)
    EditText etResume;
    @BindView(R.id.tv_num)
    TextView tvNum;

    private boolean isRequest = false;

    public static PersonResumeChangeFragment newInstance() {
        Bundle args = new Bundle();

        PersonResumeChangeFragment fragment = new PersonResumeChangeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container,
                                        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_resume_change, container, false);
    }

    @Override
    protected void initView(View view) {
        etResume.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        etResume.setSingleLine(false);
        etResume.setHorizontallyScrolling(false);
        etResume.addTextChangedListener(this);
        etResume.post(() -> {
            etResume.setText(UserCache.getDefault().getPersonalProfile());
            etResume.setSelection(UserCache.getDefault().getPersonalProfile().length());
        });
    }

    @Override
    protected void registerDagger() {
        DaggerPersonResumeChangeComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .personResumeChangeModule(new PersonResumeChangeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onSetPersonalResume() {
        hideDialog();
        isRequest = false;
        ToastUtils.show(getString(R.string.resume_change_success));
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onSetPersonalResumeError() {
        hideDialog();
        isRequest = false;
    }

    /**
     * 是否完成
     */
    public void isCommit() {
        String content = etResume.getText().toString().trim();

        if (isRequest) {
            logi("正在提交");
            return;
        }

        isRequest = true;

        showDialog();

        if (content.length() == 0) {
            content = " ";
        }
        mPresenter.setPersonalResume(content);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String content = etResume.getText().toString().trim();
        if (content.length() > 50) {
            String newStr = content.substring(0, 50);
            etResume.setText(newStr);
            Selection.setSelection(etResume.getText(), 50);
            content = newStr;
            ToastUtils.show(getString(R.string.resume_change_num_max));
        }
        tvNum.setText("" + content.length());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
