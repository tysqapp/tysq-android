package com.tysq.ty_android.feature.rewardList;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.article.RewardListResp;

public final class RewardListPresenter extends BasePresenter<RewardListView> {
    @Inject
    public RewardListPresenter(RewardListView view) {
      super(view);
    }

    public void loadRewardList(String articleId, int size, int start, boolean isFirst){
        RetrofitFactory
                .getApiService()
                .getRewardArticleList(articleId, size, start)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<RewardListResp>(mySelf) {
                  @Override
                  protected void onError(int code, String message) {
                      showErrorMsg(code, message);
                  }

                  @Override
                  protected void onSuccessRes(@NonNull RewardListResp value) {
                      mView.onLoadRewardList(value, isFirst);
                  }
                });
    }
}
