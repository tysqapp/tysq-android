package com.tysq.ty_android.feature.person.personInfoChange;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.person.personInfoChange.di.DaggerPersonInfoChangeComponent;
import com.tysq.ty_android.feature.person.personInfoChange.di.PersonInfoChangeModule;
import com.tysq.ty_android.local.sp.UserCache;

import butterknife.BindView;

/**
 * author       : frog
 * time         : 2019/5/17 下午2:40
 * desc         : 设置昵称、现居地、职位、行业
 * version      : 1.0.0
 */
public final class PersonInfoChangeFragment extends BitBaseFragment<PersonInfoChangePresenter>
        implements PersonInfoChangeView, TextWatcher {

    public static final String HINT = "hintId";
    public static final int MAXLENGTH = 10;
    //接收PersonInfoActivity传递过来的参数
    private int mhintId;

    @BindView(R.id.et_nick)
    EditText etNick;
    @BindView(R.id.iv_clear)
    ImageView ivClear;

    private boolean isRequest = false;

    public static PersonInfoChangeFragment newInstance(int hintId) {
        Bundle args = new Bundle();
        args.putInt(HINT, hintId);
        PersonInfoChangeFragment fragment = new PersonInfoChangeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        mhintId = arguments.getInt(HINT);
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container,
                                        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_info_change, container, false);
    }

    @Override
    protected void initView(View view) {
        etNick.setHint(mhintId);
        //etNick.addTextChangedListener(this);
        switch (mhintId){
            case R.string.nick_change_hint:
                etNick.post(() -> {
                    etNick.setText(UserCache.getDefault().getAccount());
                    etNick.setSelection(UserCache.getDefault().getAccount().length());
                });
                break;
            case R.string.address_change_hint:
                etNick.post(() ->{
                    etNick.setText(UserCache.getDefault().getHomeAddress());
                    etNick.setSelection(UserCache.getDefault().getHomeAddress().length());
                });
                break;

            case R.string.industry_change_hint:
                etNick.post(() ->{
                    etNick.setText(UserCache.getDefault().getTrade());
                    etNick.setSelection(UserCache.getDefault().getTrade().length());
                });
                break;

            case R.string.job_change_hint:
                etNick.post(() ->{
                    etNick.setText(UserCache.getDefault().getCareer());
                    etNick.setSelection(UserCache.getDefault().getCareer().length());
                });
                break;
        }

        ivClear.setOnClickListener(v -> etNick.setText(""));
    }

    @Override
    protected void registerDagger() {
        DaggerPersonInfoChangeComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .personInfoChangeModule(new PersonInfoChangeModule(this))
                .build()
                .inject(this);
    }

    /**
     * 是否完成
     */
    public void isCommit() {
        String nick = etNick.getText().toString().trim();

        if (TextUtils.isEmpty(nick) && mhintId == R.string.nick_change_hint) {
            ToastUtils.show(getString(mhintId));
            return;
        }

        if (isRequest) {
            logi("正在提交");
            return;
        }

        isRequest = true;

        showDialog();

        if (nick.length() == 0){

            mPresenter.updatePerson("",mhintId);
        }else{
            mPresenter.updatePerson(nick, mhintId);
        }

    }


    @Override
    public void onUpdatePersonError() {
        hideDialog();
        isRequest = false;
    }

    @Override
    public void onUpdatePerson(int response) {
        hideDialog();
        isRequest = false;
        ToastUtils.show(getString(response));
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String content = etNick.getText().toString().trim();
        if (content.length() > MAXLENGTH){
            String newStr = content.substring(0,MAXLENGTH);
            etNick.setText(newStr);
            Selection.setSelection(etNick.getText(), MAXLENGTH);
            ToastUtils.show(getString(R.string.edit_text_num_max));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
