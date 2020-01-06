package com.tysq.ty_android.feature.emailVerify;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.activity.BitBaseActivity;
import com.google.android.exoplayer2.util.Log;
import com.tysq.ty_android.R;
import com.tysq.ty_android.adapter.TextWatcherAdapter;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.TyConfig;
import com.tysq.ty_android.feature.emailVerify.di.DaggerEmailVerifyComponent;
import com.tysq.ty_android.feature.emailVerify.di.EmailVerifyModule;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import butterknife.BindView;
import eventbus.VerifyEmailEvent;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author       : frog
 * time         : 2019-07-15 11:39
 * desc         : 邮箱验证
 * version      : 1.3.0
 */
public final class EmailVerifyActivity
        extends BitBaseActivity<EmailVerifyPresenter>
        implements EmailVerifyView, View.OnClickListener {

    private static final String EMAIL = "email";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    /**
     * 邮箱
     */
    private String mEmail;

    /**
     * 邮箱验证码id
     */
    private String mCodeId;

    /**
     * 获取验证码
     */
    private boolean isObtainCode;

    private Disposable mTimer;

    public static void startActivity(Context context,
                                     String email) {
        Intent intent = new Intent(context, EmailVerifyActivity.class);

        intent.putExtra(EMAIL, email);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_email_verify;
    }

    @Override
    protected void initIntent(Intent intent) {
        mEmail = intent.getStringExtra(EMAIL);
    }

    @Override
    protected void initView() {
        isObtainCode = false;

        tvEmail.setText(String.format(getString(R.string.email_verify_info), mEmail));

        ivBack.setOnClickListener(this);
        tvSendCode.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

        tvConfirm.setEnabled(false);

        etCode.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String code = etCode.getText().toString().trim();

                boolean isEnable = true;

                if (TextUtils.isEmpty(code)) {  // 验证码验证
                    isEnable = false;
                }

                tvConfirm.setEnabled(isEnable);
            }
        });
    }

    @Override
    protected void registerDagger() {
        DaggerEmailVerifyComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .emailVerifyModule(new EmailVerifyModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_send_code: // 获取验证码
                obtainCode();
                break;

            case R.id.tv_confirm:  // 确认
                verifyEmail();
                break;

            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 验证邮箱
     */
    private void verifyEmail() {
        String code = etCode.getText().toString().trim();

        if (!checkEmail(mEmail)) {
            ToastUtils.show(getString(R.string.email_verify_format_illegal));
            return;
        }

        if (TextUtils.isEmpty(mCodeId)) {
            ToastUtils.show(getString(R.string.email_verify_code_id_illegal));
            return;
        }

        if (TextUtils.isEmpty(code)) {
            ToastUtils.show(getString(R.string.email_verify_code_illegal));
            return;
        }

        mPresenter.verifyEmail(mEmail, code, mCodeId);
    }

    /**
     * 发送验证码
     */
    private void obtainCode() {
        if (!checkEmail(mEmail)) {
            ToastUtils.show(getString(R.string.email_verify_format_illegal));
            return;
        }

        backwards();
        mPresenter.sendEmailCode(mEmail);
    }

    /**
     * 进行倒数
     */
    @MainThread
    private void backwards() {

        if (isObtainCode) {
            return;
        }

        isObtainCode = true;
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
                        isObtainCode = false;
                        setSendCodeText(0);
                        setCodeClickable(true);
                    }
                });
    }

    @Override
    public void onSendEmailCodeFailure() {
    }

    @Override
    public void onSendEmailCode(String captchaId) {
        mCodeId = captchaId;
        ToastUtils.show(getString(R.string.code_send_suc));
    }

    @Override
    public void onVerifyEmail() {
        hideDialog();

        ToastUtils.show(getString(R.string.email_verify_success));
        EventBus.getDefault().post(new VerifyEmailEvent());

        finish();
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
            tvSendCode.setText(getString(R.string.email_verify_obtain_code));
            return;
        }

        tvSendCode.setText(
                String.format(getString(R.string.email_verify_backwards),
                        value
                ));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null && !mTimer.isDisposed()) {
            mTimer.dispose();
        }
    }

}
