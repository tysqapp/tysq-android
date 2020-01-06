package com.tysq.ty_android.feature.coin.coinWithdraw;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import request.WithdrawReq;
import response.coin.WithdrawInfoResp;
import response.common.EmailCodeResp;

public final class CoinWithdrawPresenter extends BasePresenter<CoinWithdrawView> {
    @Inject
    public CoinWithdrawPresenter(CoinWithdrawView view) {
        super(view);
    }

    public void getBtcRateInfo(long amount, boolean isCommit) {
        RetrofitFactory
                .getApiService()
                .getBtcRate(amount)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<WithdrawInfoResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        if (isCommit) {
                            mView.onGetBtcRateInfoViaCommitError();
                        }
                    }

                    @Override
                    protected void onSuccessRes(@NonNull WithdrawInfoResp value) {
                        if (isCommit) {
                            mView.onGetBtcRateInfoViaCommit(amount, value);
                        } else {
                            mView.onGetBtcRateInfo(value);
                        }
                    }
                });
    }

    public void sendEmailCode(String email) {
        RetrofitFactory
                .getApiService()
                .getCaptcha(email, Constant.EmailCaptchaType.DRAW_CURRENCY)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<EmailCodeResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onSendEmailCodeError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull EmailCodeResp value) {
                        mView.onSendEmailCode(value);
                    }
                });
    }

    public void postWithdraw(String address,
                             int amountNum,
                             String remark,
                             String code,
                             String codeId) {
        RetrofitFactory
                .getApiService()
                .postWithdraw(new WithdrawReq(address,
                        amountNum,
                        remark,
                        UserCache.getDefault().getEmail(),
                        codeId,
                        code))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.hideDialog();
                        mView.onPostWithdraw();
                    }
                });
    }

}
