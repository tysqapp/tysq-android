package com.tysq.ty_android.feature.rank.orderRank;

import com.bit.view.IView;

import response.rank.RankOrderResp;

public interface OrderRankView extends IView {
    void onGetScoreOrderFailure(boolean isFirst);

    void onGetScoreOrder(RankOrderResp value, boolean isFirst);
}
