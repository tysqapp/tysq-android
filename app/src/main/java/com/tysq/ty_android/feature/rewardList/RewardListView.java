package com.tysq.ty_android.feature.rewardList;

import com.bit.view.IView;

import response.article.RewardListResp;

public interface RewardListView extends IView {

    void onLoadRewardList(RewardListResp value, boolean isFirst);
}
