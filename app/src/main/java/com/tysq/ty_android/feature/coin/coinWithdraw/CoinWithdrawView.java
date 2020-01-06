package com.tysq.ty_android.feature.coin.coinWithdraw;

import com.bit.view.IView;

import response.coin.WithdrawInfoResp;
import response.common.EmailCodeResp;

public interface CoinWithdrawView extends IView {
    void onGetBtcRateInfo(WithdrawInfoResp value);

    void onSendEmailCode(EmailCodeResp value);

    void onSendEmailCodeError();

    void onGetBtcRateInfoViaCommit(long amount, WithdrawInfoResp value);

    void onGetBtcRateInfoViaCommitError();

    void onPostWithdraw();

}
