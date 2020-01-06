package com.tysq.ty_android.feature.person.personResumeChange;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import eventbus.UserInfoUpdateEvent;
import request.PersonInfoUpdateReq;
import response.login.LoginResp;

public final class PersonResumeChangePresenter extends BasePresenter<PersonResumeChangeView> {
    @Inject
    public PersonResumeChangePresenter(PersonResumeChangeView view) {
      super(view);
    }

    public void setPersonalResume(String content){
        PersonInfoUpdateReq req = new PersonInfoUpdateReq(UserCache.getDefault().getAccount(),
                UserCache.getDefault().getIconId(),
                UserCache.getDefault().getHomeAddress(),
                UserCache.getDefault().getTrade(),
                UserCache.getDefault().getCareer(),
                content);

         RetrofitFactory
              .getApiService()
              .putUpdatePersonData(req)
              .compose(RxParser.handleSingleDataResult())
              .subscribe(new RxSingleSubscriber<LoginResp>(mySelf) {
                @Override
                protected void onError(int code, String message) {
                    showErrorMsg(code, message);
                    mView.onSetPersonalResumeError();
                }

                @Override
                protected void onSuccessRes(@NonNull LoginResp value) {
                    UserCache.save(value.getAccountInfo());
                    EventBus.getDefault().post(new UserInfoUpdateEvent());
                    mView.onSetPersonalResume();
                }
              });
    }
}
