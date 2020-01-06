package com.tysq.ty_android.feature.coin.coinWithdraw;

import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.google.android.exoplayer2.util.Log;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.activity.CommonToolbarActivity;
import com.tysq.ty_android.config.TyConfig;
import com.tysq.ty_android.feature.coin.coinWithdraw.di.CoinWithdrawModule;
import com.tysq.ty_android.feature.coin.coinWithdraw.di.DaggerCoinWithdrawComponent;
import com.tysq.ty_android.feature.coin.coinWithdrawLog.CoinWithdrawLogFragment;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.utils.TyUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import eventbus.MyCoinRefreshEvent;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import response.coin.WithdrawInfoResp;
import response.common.EmailCodeResp;

/**
 * author       : frog
 * time         : 2019-08-15 12:16
 * desc         : 金币提现
 * version      : 1.3.0
 */
public final class CoinWithdrawFragment
        extends BitBaseFragment<CoinWithdrawPresenter>
        implements CoinWithdrawView,
        CommonToolbarActivity.ICommonFragment,
        View.OnClickListener, WithdrawConfirmDialog.WithdrawListener {

    public static final String TAG = "CoinWithdrawFragment";

    private static final String COIN_AMOUNT = "coin_amount";

    private static final DecimalFormat DF = new DecimalFormat("0.########");

    @BindView(R.id.et_address)
    EditText etAddress;

    @BindView(R.id.tv_max_tip)
    TextView tvMaxTip;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_btc)
    TextView tvBtc;
    @BindView(R.id.et_amount)
    EditText etAmount;

    @BindView(R.id.tv_obtain_code)
    TextView tvObtainCode;
    @BindView(R.id.et_code)
    EditText etCode;

    @BindView(R.id.et_remark)
    EditText etRemark;

    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.tv_result)
    TextView tvResult;

    @BindView(R.id.tv_commit)
    TextView tvCommit;

    // 金币量
    private long coinAmount;

    /**
     * 邮箱验证码id
     */
    private String mCodeId;

    /**
     * 获取验证码
     */
    private boolean isObtainCode;

    private Disposable mTimer;

    private WithdrawConfirmDialog mWithdrawConfirmDialog;

    public static CoinWithdrawFragment newInstance(long coinAmount) {

        Bundle args = new Bundle();

        CoinWithdrawFragment fragment = new CoinWithdrawFragment();
        fragment.setArguments(args);

        args.putLong(COIN_AMOUNT, coinAmount);

        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container,
                                        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coin_withdraw, container, false);
    }

    @Override
    protected void initArgs(Bundle arguments) {
        coinAmount = arguments.getLong(COIN_AMOUNT);
    }

    @Override
    protected void initView(View view) {

        tvMaxTip.setText(String.format(getString(R.string.withdraw_max_amount),
                TyUtils.formatDotNum(coinAmount)));

        setCoinAboutInfo(0, 0, 0);

        tvAll.setOnClickListener(this);
        tvObtainCode.setOnClickListener(this);
        tvCommit.setOnClickListener(this);

        etAmount.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                loadBtcInfo();
            }
        });

    }

    private void loadBtcInfo() {
        String amount = etAmount.getText().toString().trim();
        if (TextUtils.isEmpty(amount)) {
            return;
        }

        int amountNum = getAmountNum(amount);

        if (amountNum <= 0) {
            return;
        }

        mPresenter.getBtcRateInfo(amountNum, false);
    }

    private void setCoinAboutInfo(double btc,
                                  double fee,
                                  double result) {


        tvBtc.setText(String.format(getString(R.string.withdraw_btc), DF.format(btc)));

        tvFee.setText(String.format(getString(R.string.withdraw_fee), DF.format(fee)));

        if (result <= 0) {
            tvResult.setText(String.format(getString(R.string.withdraw_result), "0"));
        } else {
            tvResult.setText(String.format(getString(R.string.withdraw_result), DF.format(result)));
        }
    }

    @Override
    protected void registerDagger() {
        DaggerCoinWithdrawComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .coinWithdrawModule(new CoinWithdrawModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getTitleId() {
        return R.string.withdraw;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_all:
                etAmount.setText(coinAmount + "");
                loadBtcInfo();
                break;
            case R.id.tv_obtain_code:
                sendCode();
                loadBtcInfo();
                break;
            case R.id.tv_commit:

                int amountNum = checkData();

                if (amountNum <= 0) {
                    return;
                }

                showDialog();
                mPresenter.getBtcRateInfo(amountNum, true);

                break;
        }
    }

    private int checkData() {

        String address = etAddress.getText().toString().trim();
        String amount = etAmount.getText().toString().trim();
        String code = etCode.getText().toString().trim();

        if (TextUtils.isEmpty(address)) {
            ToastUtils.show(getString(R.string.withdraw_enter_address));
            return -1;
        }

        int amountNum = getAmountNum(amount);
        if (amountNum <= 0) {
            ToastUtils.show(getString(R.string.withdraw_enter_amount));
            return -1;
        }

        if (TextUtils.isEmpty(code) || TextUtils.isEmpty(mCodeId)) {
            ToastUtils.show(getString(R.string.withdraw_enter_code));
            return -1;
        }

        return amountNum;

    }

    /**
     * 进行倒数
     */
    @MainThread
    private void sendCode() {

        if (isObtainCode) {
            return;
        }

        isObtainCode = true;
        setSendCodeText(TyConfig.TIME_LONG);
        setCodeClickable(false);

        mPresenter.sendEmailCode(UserCache.getDefault().getEmail());

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
            tvObtainCode.setText(getString(R.string.withdraw_obtain_email_code));
            return;
        }

        tvObtainCode.setText(
                String.format(getString(R.string.withdraw_verify_backwards),
                        value
                ));
    }

    /**
     * 转换数量
     *
     * @param amount
     * @return
     */
    private int getAmountNum(String amount) {
        int amountNum;
        try {
            amountNum = Integer.parseInt(amount);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return amountNum;
    }

    /**
     * "发送验证码" 是否可以可以点击
     *
     * @param clickable true：可以点击；false：不可点击
     */
    private void setCodeClickable(boolean clickable) {
        tvObtainCode.setEnabled(clickable);
        if (clickable) {
            tvObtainCode.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
        } else {
            tvObtainCode.setTextColor(ContextCompat.getColor(getContext(), R.color.et_tip_text_color));
        }
    }

    @Override
    public void onGetBtcRateInfo(WithdrawInfoResp value) {
        setCoinAboutInfo(value.getWithdrawAmount(), value.getMinFee(), value.getIncomeAmount());
    }

    @Override
    public void onSendEmailCode(EmailCodeResp value) {
        mCodeId = value.getCaptchaId();
        ToastUtils.show(getString(R.string.code_send_suc));
    }

    @Override
    public void onSendEmailCodeError() {
        mTimer.dispose();
    }

    @Override
    public void onGetBtcRateInfoViaCommit(long amount,
                                          WithdrawInfoResp value) {
        hideDialog();
        if (mWithdrawConfirmDialog == null) {
            mWithdrawConfirmDialog = WithdrawConfirmDialog.newInstance();
        }

        String income;
        if (value.getIncomeAmount() <= 0) {
            income = "0";
        } else {
            income = DF.format(value.getIncomeAmount());
        }
        mWithdrawConfirmDialog.setInfo(amount,
                DF.format(value.getWithdrawAmount()),
                DF.format(value.getMinFee()),
                income);

        setCoinAboutInfo(value.getWithdrawAmount(),
                value.getMinFee(),
                value.getIncomeAmount());

        mWithdrawConfirmDialog.setListener(this);

        mWithdrawConfirmDialog.show(this);
    }

    @Override
    public void onGetBtcRateInfoViaCommitError() {
        hideDialog();
    }

    @Override
    public void onPostWithdraw() {

        ToastUtils.show(getString(R.string.withdraw_success));

        CommonToolbarActivity.startActivity(getContext(), CoinWithdrawLogFragment.TAG);

        // 刷新我的金币
        EventBus.getDefault().post(new MyCoinRefreshEvent());

        if (getActivity() != null) {
            getActivity().finish();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null && !mTimer.isDisposed()) {
            mTimer.dispose();
        }
    }

    @Override
    public void onConfirmCommit() {
        int amountNum = checkData();

        if (amountNum <= 0) {
            return;
        }

        showDialog();

        String address = etAddress.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String remark = etRemark.getText().toString().trim();

        mPresenter.postWithdraw(address, amountNum, remark, code, mCodeId);

    }
}
