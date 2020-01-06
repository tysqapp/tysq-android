package com.tysq.ty_android.feature.personalHomePage;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import request.AttentionReq;
import response.personal.PersonalPageResp;

public final class PersonalHomePagePresenter extends BasePresenter<PersonalHomePageView> {
    @Inject
    public PersonalHomePagePresenter(PersonalHomePageView view) {
      super(view);
    }

    public void getPersonalPage(int accountId){
        RetrofitFactory
                .getApiService()
                .getPersonalPage(accountId)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<PersonalPageResp>(mySelf) {
                  @Override
                  protected void onError(int code, String message) {
                      showErrorMsg(code, message);
                  }

                  @Override
                  protected void onSuccessRes(@NonNull PersonalPageResp value) {
                      mView.getPersonalPage(value);
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
