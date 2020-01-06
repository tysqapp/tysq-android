package com.tysq.ty_android.feature.myFans;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import request.AttentionReq;
import response.MyAttentionListResp;
import response.MyFansListResp;

public final class MyFansPresenter extends BasePresenter<MyFansView> {
    @Inject
    public MyFansPresenter(MyFansView view) {
      super(view);
    }

    public void getMyFansList(int accountId, int start, int size, boolean isFirst){
      RetrofitFactory
              .getApiService()
              .getFansList(accountId, start, size)
              .compose(RxParser.handleSingleDataResult())
              .subscribe(new RxSingleSubscriber<MyFansListResp>(mySelf) {
                  @Override
                  protected void onError(int code, String message) {
                      showErrorMsg(code, message);
                  }

                  @Override
                  protected void onSuccessRes(@NonNull MyFansListResp value) {
                        mView.hideDialog();
                        mView.onGetMyFansList(isFirst,value.getTotalNum(), value.getAttentionInfo());
                  }
              });
    }

    public void postAttention(int attentionId, boolean isFollow){
        RetrofitFactory
                .getApiService()
                .postAttention(new AttentionReq(attentionId, isFollow))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.postAttention();
                    }
                });
    }

}
