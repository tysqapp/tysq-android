package com.tysq.ty_android.feature.forget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.adapter.TextWatcherAdapter;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.TyConfig;
import com.tysq.ty_android.feature.forget.di.DaggerForgetComponent;
import com.tysq.ty_android.feature.forget.di.ForgetModule;
import com.tysq.ty_android.feature.login.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import butterknife.BindView;
import eventbus.LoginStateChangeEvent;
import eventbus.LoginSucEvent;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author       : frog
 * time         : 2019/6/5 下午2:10
 * desc         : 忘记密码
 * version      : 1.3.0
 */
public final class ForgetActivity extends BitBaseActivity<ForgetPresenter>
        implements ForgetView, View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_email)
    RelativeLayout rlEmail;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_next_step)
    TextView tvNextStep;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.rl_pwd)
    RelativeLayout rlPwd;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.et_pwd_new)
    EditText etPwdNew;
    @BindView(R.id.et_pwd_confirm)
    EditText etPwdConfirm;
    @BindView(R.id.iv_pwd_state)
    ImageView ivPwdState;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    private boolean isHidePwd;
    private boolean isSendCode;

    private Disposable mTimer;

    /**
     * 邮箱验证码id
     */
    private String mCodeId;

    public static void startActivity(Context context, String email) {
        Intent intent = new Intent(context, ForgetActivity.class);
        if (!TextUtils.isEmpty(email)) {
            intent.putExtra(TyConfig.EMAIL, email);
        }

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_forget;
    }

    @Override
    protected void initIntent(Intent intent) {
        String email = intent.getStringExtra(TyConfig.EMAIL);
        if (TextUtils.isEmpty(email)) {
            email = "";
        }

        etEmail.setText(email);
    }

    @Override
    protected void initView() {
        isHidePwd = true;
        isSendCode = false;

        changeNextStepBtnClickable();
        tvConfirm.setEnabled(false);

        tvSendCode.setOnClickListener(this);
        tvNextStep.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivPwdState.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

        etEmail.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString().trim();
                boolean isMatch = checkEmail(email);
                setCodeClickable(isMatch);

                changeNextStepBtnClickable();
            }
        });

        etCode.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                changeNextStepBtnClickable();
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    return;
                }
                String email = getEmailInfo();
                boolean isMatch = checkEmail(email);
                if (!isMatch) {
                    ToastUtils.show(getString(R.string.email_format_illegal));
                }
            }
        });

        etPwdNew.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                checkPwdVerify();
            }
        });

        etPwdConfirm.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                checkPwdVerify();
            }
        });

    }

    /**
     * 邮箱 和 验证码均有填才让 "下一步" 可点击
     */
    private void changeNextStepBtnClickable() {
        String email = getEmailInfo();
        String code = etCode.getText().toString().trim();

        boolean isEnable = true;

        if (!checkEmail(email)) {
            isEnable = false;
        } else if (TextUtils.isEmpty(code)) {
            isEnable = false;
        }

        tvNextStep.setEnabled(isEnable);
    }

    /**
     * 校验两个密码是否有输入，有的话才可以让 "确定" 可点击
     */
    private void checkPwdVerify() {
        String pwd1 = etPwdNew.getText().toString();
        String pwd2 = etPwdConfirm.getText().toString();
        if (pwd1.length() > 0 && pwd2.length() > 0) {
            tvConfirm.setEnabled(true);
        } else {
            tvConfirm.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send_code:
                sendCode();
                break;

            case R.id.tv_next_step:
                nextStep();
                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_pwd_state:
                isHidePwd = !isHidePwd;
                int startNew = etPwdNew.getSelectionStart();
                int startConfirm = etPwdConfirm.getSelectionStart();
                if (isHidePwd) {
                    ivPwdState.setImageDrawable(
                            ContextCompat.getDrawable(this, R.drawable.ic_pwd_show));
                    etPwdNew.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPwdConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    ivPwdState.setImageDrawable(
                            ContextCompat.getDrawable(this, R.drawable.ic_pwd_hide));
                    etPwdNew.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPwdConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                etPwdNew.setSelection(startNew);
                etPwdConfirm.setSelection(startConfirm);
                break;

            case R.id.tv_confirm:
                resetPwd();
                break;
        }
    }

    /**
     * 重置密码
     */
    private void resetPwd() {
        String email = getEmailInfo();
        String code = etCode.getText().toString().trim();
        String pwd1 = etPwdNew.getText().toString().trim();
        String pwd2 = etPwdConfirm.getText().toString().trim();

        if (!checkEmail(email)) {
            ToastUtils.show(getString(R.string.email_format_illegal));
            return;
        }

        if (TextUtils.isEmpty(code)) {
            ToastUtils.show(getString(R.string.code_not_null));
            return;
        }

        if (pwd1.length() < TyConfig.PWD_MIN_LENGTH
                || pwd2.length() < TyConfig.PWD_MIN_LENGTH) {
            ToastUtils.show(getString(R.string.pwd_length_not_enough));
            return;
        }

        if (!pwd1.equals(pwd2)) {
            ToastUtils.show(getString(R.string.pwd_not_equal));
            return;
        }

        showDialog();
        mPresenter.resetPwd(email, pwd1, pwd2, code, mCodeId);

    }

    /**
     * 发送验证码
     */
    private void sendCode() {
        String email = getEmailInfo();
        if (!checkEmail(email)) {
            ToastUtils.show(getString(R.string.email_format_illegal));
            return;
        }

        backwards();
        mPresenter.sendEmailCode(email);
    }

    /**
     * 下一步逻辑，验证 邮箱和验证码是否一致
     */
    private void nextStep() {
        String email = getEmailInfo();
        String code = etCode.getText().toString().trim();

        if (!checkEmail(email)) {
            ToastUtils.show(getString(R.string.email_format_illegal));
            return;
        }

        if (TextUtils.isEmpty(code)) {
            ToastUtils.show(getString(R.string.code_not_null));
            return;
        }

        showDialog();
        mPresenter.checkEmailVerify(email, code, mCodeId);

    }

    /**
     * 转换至密码输入页面
     */
    private void transformToPwd() {
        tvEmail.setText(String.format(getString(R.string.forget_email_info),
                getEmailInfo()));

        stopTimer();
        showPwd();
    }

    private void showEmail() {
        rlEmail.setVisibility(View.VISIBLE);
        rlPwd.setVisibility(View.GONE);
    }

    private void showPwd() {
        rlEmail.setVisibility(View.GONE);
        rlPwd.setVisibility(View.VISIBLE);
    }

    /**
     * 检测邮箱
     *
     * @param email 邮箱，格式：xxx@xx.xx
     * @return true：格式正确；false：格式错误
     */
    private boolean checkEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        return Pattern.matches(TyConfig.EMAIL_PARENT, email);
    }

    /**
     * 设置倒数时间
     *
     * @param value 显示的值。
     *              1、为0时，显示为 发送验证码
     *              2、其他数字，则正常显示
     */
    @MainThread
    private void setSendCodeText(long value) {
        if (value <= 0) {
            tvSendCode.setText(getString(R.string.forget_send_code));
            return;
        }

        tvSendCode.setText(
                String.format(getString(R.string.forget_backwards),
                        value
                ));
    }

    /**
     * 进行倒数
     */
    @MainThread
    private void backwards() {

        if (isSendCode) {
            return;
        }

        isSendCode = true;
        setSendCodeText(TyConfig.TIME_LONG);
        setCodeClickable(false);

        Observable
                .interval(1, TimeUnit.SECONDS)
                //一分钟的定时器
                .take(TyConfig.TIME_LONG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mTimer = d;
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.i(TAG, "onNext: " + value);
                        setSendCodeText(TyConfig.TIME_LONG - value - 1);
                        setCodeClickable(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        isSendCode = false;
                        setSendCodeText(0);
                        setCodeClickable(true);
                    }
                });
    }

    /**
     * "发送验证码" 是否可以可以点击
     *
     * @param clickable true：可以点击；false：不可点击
     */
    private void setCodeClickable(boolean clickable) {
        tvSendCode.setEnabled(clickable);
        if (clickable) {
            tvSendCode.setTextColor(ContextCompat.getColor(this, R.color.main_blue_color));
        } else {
            tvSendCode.setTextColor(ContextCompat.getColor(this, R.color.et_tip_text_color));
        }
    }

    private void stopTimer() {
        if (mTimer != null && !mTimer.isDisposed()) {
            mTimer.dispose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    @Override
    protected void registerDagger() {
        DaggerForgetComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .forgetModule(new ForgetModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onSendEmailCode(String captchaId) {
        ToastUtils.show(getString(R.string.code_send_suc));
        this.mCodeId = captchaId;
    }

    @Override
    public void onCheckEmailVerifyError() {
        hideDialog();
    }

    @Override
    public void onCheckEmailVerify() {
        hideDialog();
        transformToPwd();
    }

    @Override
    public void onResetPwdFailure() {
        hideDialog();
    }

    @Override
    public void onLoginFailure(String email) {
        hideDialog();
        finish();
        LoginActivity.startActivity(this, email);
    }

    @Override
    public void onLoginSuc() {
        hideDialog();
        ToastUtils.show(getString(R.string.forget_reset_suc));
//        MainActivity.startActivity(this);
        EventBus.getDefault().post(new LoginSucEvent());
        EventBus.getDefault().post(new LoginStateChangeEvent());
    }

    private String getEmailInfo() {
        return etEmail.getText().toString().trim();
    }

    protected int getInitRegister() {
        return BUTTER_KNIFE | EVENT_BUS;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishMySelf(LoginSucEvent event) {
        finish();
    }

}
