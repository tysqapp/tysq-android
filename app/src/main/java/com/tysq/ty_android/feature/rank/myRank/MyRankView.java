package com.tysq.ty_android.feature.rank.myRank;

import com.bit.view.IView;

import response.rank.RankDetailResp;

public interface MyRankView extends IView {
    void onLoadRankDetailFailure(boolean isFirst);

    void onLoadRankDetail(RankDetailResp value, boolean isFirst);
}
