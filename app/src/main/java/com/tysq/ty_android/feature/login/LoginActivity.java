package com.tysq.ty_android.feature.login;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.MainThread;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.event.CloseEvent;
import com.bit.event.ReloadEvent;
import com.bit.view.activity.BitBaseActivity;
import com.tysq.ty_android.R;
import com.tysq.ty_android.adapter.TextWatcherAdapter;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.NetCode;
import com.tysq.ty_android.config.TyConfig;
import com.tysq.ty_android.feature.forget.ForgetActivity;
import com.tysq.ty_android.feature.login.di.DaggerLoginComponent;
import com.tysq.ty_android.feature.login.di.LoginModule;
import com.tysq.ty_android.feature.register.RegisterActivity;
import com.tysq.ty_android.utils.TyUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Pattern;

import butterknife.BindView;
import eventbus.LoginStateChangeEvent;
import eventbus.LoginSucEvent;

/**
 * author       : frog
 * time         : 2019/6/5 下午2:10
 * desc         : 登录
 * version      : 1.3.0
 */

public final class LoginActivity extends BitBaseActivity<LoginPresenter>
        implements LoginView, View.OnClickListener {

    private static final String FROM = "from";
    private static final String VIEW_ID = "viewId";

    // 自主
    private static final int DIY = 1;
    // 切面
    private static final int AOP = 2;
    // 失效
    private static final int INVALIDATE = 3;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.iv_pwd_state)
    ImageView ivPwdState;
    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;
    @BindView(R.id.iv_code)
    ImageView ivCode;

    private boolean isRefresh;
    private boolean isHidePwd;
    private boolean isNeedCode;

    private Bitmap mCodeBitmap;

    private String mCodeId = "";

    private ObjectAnimator mRefreshAnim;

    private int mFrom;
    // 唤起该页面的view的id
    private String mOpenViewId;

    public static void startActivity(Context context, String email) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (!TextUtils.isEmpty(email)) {
            intent.putExtra(TyConfig.EMAIL, email);
        }

        intent.putExtra(FROM, DIY);

        context.startActivity(intent);
    }

    public static void startActivityForAOP(Context context,
                                           String email) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (!TextUtils.isEmpty(email)) {
            intent.putExtra(TyConfig.EMAIL, email);
        }

        intent.putExtra(FROM, AOP);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    public static void startActivityForInvalidate(Context context,
                                                  String email,
                                                  String viewId) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (!TextUtils.isEmpty(email)) {
            intent.putExtra(TyConfig.EMAIL, email);
        }

        intent.putExtra(FROM, INVALIDATE);
        intent.putExtra(VIEW_ID, viewId);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setEmailInfo(intent);

    }

    @Override
    protected void initIntent(Intent intent) {
        setEmailInfo(intent);
        mFrom = intent.getIntExtra(FROM, DIY);
        mOpenViewId = intent.getStringExtra(VIEW_ID);
    }

    private void setEmailInfo(Intent intent) {
        String email = intent.getStringExtra(TyConfig.EMAIL);
        if (TextUtils.isEmpty(email)) {
            email = "";
        }
        etEmail.setText(email);
    }

    @Override
    protected void initView() {

        isHidePwd = true;
        isNeedCode = false;

        mRefreshAnim = ObjectAnimator.ofFloat(ivRefresh, "rotation", 0, 360);
        mRefreshAnim.setDuration(2_000);
        mRefreshAnim.setInterpolator(new LinearInterpolator());
        mRefreshAnim.setRepeatCount(ValueAnimator.INFINITE);

        ivBack.setOnClickListener(this);
        ivRefresh.setOnClickListener(this);
        ivPwdState.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

        changeLoginBtnClickable();

        etEmail.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                changeLoginBtnClickable();
            }
        });

        etCode.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                changeLoginBtnClickable();
            }
        });

        etPwd.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                changeLoginBtnClickable();
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

    }

    /**
     * 更改登录按钮是否可点击
     */
    private void changeLoginBtnClickable() {
        String email = getEmailInfo();
        String pwd = etPwd.getText().toString().trim();
        String code = etCode.getText().toString().trim();

        boolean isShow = true;

        if (!Pattern.matches(TyConfig.EMAIL_PARENT, email)) {
            isShow = false;
        } else if (pwd.length() < TyConfig.PWD_MIN_LENGTH) {
            isShow = false;
        } else if (isNeedCode && TextUtils.isEmpty(code)) {
            isShow = false;
        }

        tvLogin.setEnabled(isShow);
    }

    @Override
    protected void registerDagger() {
        DaggerLoginComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCodeBitmap != null) {
            mCodeBitmap.recycle();
        }
    }

    @MainThread
    private void loadCode() {
        if (isRefresh) {
            return;
        }
        isRefresh = true;
        mRefreshAnim.start();
        mPresenter.loadCode();
    }

    @MainThread
    private void loadCodeOver() {
        isRefresh = false;
        mRefreshAnim.cancel();
    }

    // ================================== 回调 ===================================
    @Override
    public void onLoad(String codeId, String base64) {

        this.mCodeId = codeId;

        if (mCodeBitmap != null) {
            mCodeBitmap.recycle();
        }

        byte[] bitmapArray = Base64.decode(TyUtils.clipBase64(base64), Base64.DEFAULT);
        mCodeBitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);

        ivCode.setImageBitmap(mCodeBitmap);

        loadCodeOver();

    }

    @Override
    public void onLoadFail() {
        loadCodeOver();
    }

    @Override
    public void onLoginFailure(int code, String message) {
        hideDialog();

        // 显示验证码
        if (code == NetCode.LOGIN_FAIL_ERROR_CODE ||
                code == NetCode.NO_CAPTCHA_ERROR_CODE) {
            showCode();
        }

        if (isNeedCode) {
            loadCode();
        }
    }

    @Override
    public void onLogin() {
        hideDialog();
        ToastUtils.show(getString(R.string.login_success));
        EventBus.getDefault().post(new LoginSucEvent());
        EventBus.getDefault().post(new LoginStateChangeEvent());

//        JerryDownload.setOkHttp(OkHttpHelper.getOkHttpInstance());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishPage(false);
                break;
            case R.id.iv_refresh:
                loadCode();
                break;
            case R.id.iv_pwd_state:
                isHidePwd = !isHidePwd;

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

                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_register:
                RegisterActivity.startActivity(this, getEmailInfo());
                break;
            case R.id.tv_forget_pwd:
                ForgetActivity.startActivity(this, getEmailInfo());
                break;
        }
    }

    /**
     * 登录逻辑
     */
    private void login() {
        String email = getEmailInfo();
        String pwd = etPwd.getText().toString().trim();
        String code = etCode.getText().toString().trim();

        if (!Pattern.matches(TyConfig.EMAIL_PARENT, email)) {
            ToastUtils.show(getString(R.string.email_format_illegal));
            return;
        }

        if (pwd.length() < TyConfig.PWD_MIN_LENGTH) {
            ToastUtils.show(getString(R.string.pwd_length_not_enough));
            return;
        }

        if (isNeedCode && TextUtils.isEmpty(code)) {
            ToastUtils.show(getString(R.string.code_not_null));
            return;
        }

        showDialog();
        mPresenter.login(email, pwd, code, mCodeId);

    }

    /**
     * 显示验证码
     */
    private void showCode() {
        loadCode();
        isNeedCode = true;
        etCode.setVisibility(View.VISIBLE);
        ivCode.setVisibility(View.VISIBLE);
        ivRefresh.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏验证码
     */
    private void hideCode() {
        isNeedCode = false;
        mCodeId = "";
        etCode.setVisibility(View.GONE);
        ivCode.setVisibility(View.GONE);
        ivRefresh.setVisibility(View.GONE);
    }

    /**
     * 获取邮箱输入框内容
     */
    private String getEmailInfo() {
        return etEmail.getText().toString().trim();
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

    protected int getInitRegister() {
        return BUTTER_KNIFE | EVENT_BUS;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishMySelf(LoginSucEvent event) {
        finishPage(true);
    }

    @Override
    public void onBackPressed() {
        finishPage(false);
    }

    private void finishPage(boolean isReload) {

        if (mFrom == INVALIDATE) {

            if (isReload) {
                EventBus.getDefault().post(new ReloadEvent(mOpenViewId));
            } else {
                EventBus.getDefault().post(new CloseEvent(mOpenViewId));
            }

        }

        finish();

    }

}
