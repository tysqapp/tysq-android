package com.tysq.ty_android.feature.register;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.adapter.TextWatcherAdapter;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.config.TyConfig;
import com.tysq.ty_android.feature.emailVerify.EmailVerifyActivity;
import com.tysq.ty_android.feature.login.LoginActivity;
import com.tysq.ty_android.feature.register.di.DaggerRegisterComponent;
import com.tysq.ty_android.feature.register.di.RegisterModule;
import com.tysq.ty_android.feature.web.TyWebViewActivity;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.utils.TyUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Pattern;

import butterknife.BindView;
import eventbus.LoginSucEvent;
import response.login.RegisterResp;
import response.login.RespCaptcha;

/**
 * author       : frog
 * time         : 2019/6/5 下午2:10
 * desc         : 注册
 * version      : 1.3.0
 */
public final class RegisterActivity
        extends BitBaseActivity<RegisterPresenter>
        implements RegisterView, View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_referral_code)
    EditText etReferralCode;

    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;
    @BindView(R.id.iv_code)
    ImageView ivCode;

    @BindView(R.id.cb_protocol)
    CheckBox cbProtocol;
    @BindView(R.id.tv_read_agree)
    TextView tvReadAgree;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.iv_pwd_state)
    ImageView ivPwdState;
    @BindView(R.id.tv_protocol)
    TextView tvProtocol;

    /**
     * 是否隐藏密码
     */
    private boolean isHidePwd;

    /**
     * 验证码id
     */
    private String mCodeId = "";
    private ObjectAnimator mRefreshAnim;
    private boolean isLoadingCode;
    private Bitmap mCodeBitmap;

    /**
     * @param email 邮箱，可为空，即 "" 或 null
     */
    public static void startActivity(Context context, String email) {
        Intent intent = new Intent(context, RegisterActivity.class);
        if (!TextUtils.isEmpty(email)) {
            intent.putExtra(TyConfig.EMAIL, email);
        }

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setEmailInfo(intent);
    }

    @Override
    protected void initIntent(Intent intent) {
        setEmailInfo(intent);
    }

    @Override
    protected void initView() {

        isLoadingCode = true;

        mRefreshAnim = ObjectAnimator.ofFloat(ivRefresh, "rotation", 0, 360);
        mRefreshAnim.setDuration(2_000);
        mRefreshAnim.setInterpolator(new LinearInterpolator());
        mRefreshAnim.setRepeatCount(ValueAnimator.INFINITE);
        mRefreshAnim.start();

        TextWatcherAdapter textWatcherAdapter = new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                changeRegisterBtnClickable();
            }
        };

        isHidePwd = true;

        ivBack.setOnClickListener(this);
        ivPwdState.setOnClickListener(this);

        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvProtocol.setOnClickListener(this);
        ivRefresh.setOnClickListener(this);

        changeRegisterBtnClickable();

        etPwd.addTextChangedListener(textWatcherAdapter);
        etReferralCode.addTextChangedListener(textWatcherAdapter);
        etVerifyCode.addTextChangedListener(textWatcherAdapter);

        cbProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeRegisterBtnClickable();
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

        mPresenter.getVerifyCode();

    }

    private void changeRegisterBtnClickable() {
        String email = getEmailInfo();
        String pwd = etPwd.getText().toString().trim();
        String verifyCode = etVerifyCode.getText().toString().trim();

        boolean isEnable = true;

        if (!checkEmail(email)) { // 邮箱验证
            isEnable = false;
        } else if (pwd.length() < TyConfig.PWD_MIN_LENGTH) { // 密码验证
            isEnable = false;
        } else if (TextUtils.isEmpty(verifyCode) || TextUtils.isEmpty(mCodeId)) { // 验证码验证
            isEnable = false;
        } else if (!cbProtocol.isChecked()) { // 用户协议验证
            isEnable = false;
        }

        tvRegister.setEnabled(isEnable);
    }

    @Override
    protected void registerDagger() {
        DaggerRegisterComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .registerModule(new RegisterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_pwd_state:
                isHidePwd = !isHidePwd;
                setPwdState(isHidePwd);
                break;

            case R.id.tv_login:
                LoginActivity.startActivity(this, getEmailInfo());
                finish();
                break;

            case R.id.tv_register:
                register();
                break;

            case R.id.tv_protocol:
                TyWebViewActivity.startActivity(this, Constant.HtmlAPI.USER_PROTOCOL);
                break;

            case R.id.iv_refresh:
                if(isLoadingCode) {
                    return;
                }
                isLoadingCode = true;
                mRefreshAnim.start();
                mPresenter.getVerifyCode();
                break;

        }
    }

    /**
     * 进入注册逻辑
     */
    private void register() {
        String email = getEmailInfo();
        String pwd = etPwd.getText().toString().trim();
        String code = etReferralCode.getText().toString().trim();
        String verifyCode = etVerifyCode.getText().toString().trim();

        // 邮箱验证
        if (!checkEmail(email)) {
            ToastUtils.show(getString(R.string.email_format_illegal));
            return;
        }

        // 密码验证
        if (pwd.length() < TyConfig.PWD_MIN_LENGTH) {
            ToastUtils.show(getString(R.string.pwd_length_not_enough));
            return;
        }

        // 用户协议验证
        if (!cbProtocol.isChecked()) {
            ToastUtils.show(getString(R.string.protocol_read_tip));
            return;
        }

        showDialog();
        mPresenter.register(email, pwd, code, mCodeId, verifyCode);

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
     * 设置是否要显示密码
     *
     * @param isHidePwd true：隐藏密码
     *                  false： 显示密码
     */
    private void setPwdState(boolean isHidePwd) {
        int start = etPwd.getSelectionStart();
        if (isHidePwd) {
            ivPwdState.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_pwd_show));
            etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            ivPwdState.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_pwd_hide));
            etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        etPwd.setSelection(start);
    }

    /**
     * 从 intent 获取邮箱信息，并填入
     */
    private void setEmailInfo(Intent intent) {
        String email = intent.getStringExtra(TyConfig.EMAIL);

        if (TextUtils.isEmpty(email)) {
            email = "";
        }
        etEmail.setText(email);

    }

    @Override
    public void onRegisterFailure() {
        hideDialog();
    }


    @Override
    public void onLoginSuc(RegisterResp registerResp) {
        hideDialog();

        showEmailVerifyDialog(registerResp);
    }

    /**
     * 显示邮箱验证验证
     *
     * @param registerResp
     */
    private void showEmailVerifyDialog(RegisterResp registerResp) {

        String content = String.format(getString(R.string.email_verify_tip_content), registerResp.getActivateEmailScore());

        new MaterialDialog.Builder(this)
                .title(getString(R.string.email_verify_tip_title))
                .content(content)
                .negativeColor(ContextCompat.getColor(this, R.color.et_tip_text_color))
                .negativeText(getString(R.string.email_verify_skip))
                .onNegative((dialog, which) -> {
                    // 发送登录成功事件，会关闭 "登录"、"注册"、"忘记密码" 页
                    EventBus.getDefault().post(new LoginSucEvent());
                })
                .positiveColor(ContextCompat.getColor(this, R.color.main_blue_color))
                .positiveText(getString(R.string.email_verify_verify))
                .onPositive((dialog, which) -> {

                    EmailVerifyActivity
                            .startActivity(this, UserCache.getDefault().getEmail());

                    // 发送登录成功事件，会关闭 "登录"、"注册"、"忘记密码" 页
                    EventBus.getDefault().post(new LoginSucEvent());
                })
                .show();
    }

    /**
     * 登录失败，跳至登录页
     *
     * @param email 邮箱
     */
    @Override
    public void onLoginFailure(String email) {
        hideDialog();
        finish();
        LoginActivity.startActivity(this, email);
    }

    @Override
    public void onGetVerifyCodeError() {
        loadOver();
    }

    @Override
    public void onGetVerifyCode(RespCaptcha value) {
        mCodeId = value.getCaptchaId();

        if (mCodeBitmap != null) {
            mCodeBitmap.recycle();
        }

        byte[] bitmapArray =
                Base64.decode(TyUtils.clipBase64(value.getCaptchaBase64()), Base64.DEFAULT);
        mCodeBitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        ivCode.setImageBitmap(mCodeBitmap);

        loadOver();
    }

    private void loadOver() {
        mRefreshAnim.cancel();
        isLoadingCode = false;
    }

    /**
     * 获取邮箱信息
     */
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCodeBitmap != null) {
            mCodeBitmap.recycle();
        }
    }
}
