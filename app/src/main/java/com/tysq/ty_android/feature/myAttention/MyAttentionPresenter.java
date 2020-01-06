package com.tysq.ty_android.feature.myAttention;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import request.AttentionReq;
import response.MyAttentionListResp;

public final class MyAttentionPresenter extends BasePresenter<MyAttentionView> {


    @Inject
    public MyAttentionPresenter(MyAttentionView view) {
      super(view);
    }

    public void getMyAttentionList(int accountId, int start, int size, boolean isFirst){
        RetrofitFactory
                .getApiService()
                .getAttentionList(accountId, start, size)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<MyAttentionListResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.showDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull MyAttentionListResp value) {
                        mView.hideDialog();
                        mView.onGetMyAttentionList(isFirst, value.getTotalNum(), value.getAttentionInfo());
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
