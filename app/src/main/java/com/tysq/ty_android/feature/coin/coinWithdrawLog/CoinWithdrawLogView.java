package com.tysq.ty_android.feature.coin.coinWithdrawLog;

import com.bit.view.IView;

import java.util.List;

import response.coin.WithdrawLogResp;

public interface CoinWithdrawLogView extends IView {
    void onGetWithdrawLogError(boolean isFirst);

    void onGetWithdrawLog(boolean isFirst,
                          List<WithdrawLogResp.WithdrawReviewListBean> withdrawReviewList);
}
