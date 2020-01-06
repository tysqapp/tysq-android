package com.tysq.ty_android.feature.person.personInfoChange;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.R;
import com.tysq.ty_android.local.sp.UserCache;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import cache.User;
import eventbus.UserInfoUpdateEvent;
import request.PersonInfoUpdateReq;
import response.login.LoginResp;

public final class PersonInfoChangePresenter extends BasePresenter<PersonInfoChangeView> {

    private int responseToast;
    @Inject
    public PersonInfoChangePresenter(PersonInfoChangeView view) {
        super(view);
    }


    public void updatePerson(String content, int resId){

        PersonInfoUpdateReq req = new PersonInfoUpdateReq(UserCache.getDefault().getAccount(),
                UserCache.getDefault().getIconId(),
                UserCache.getDefault().getHomeAddress(),
                UserCache.getDefault().getTrade(),
                UserCache.getDefault().getCareer(),
                UserCache.getDefault().getPersonalProfile());


        switch (resId){
            case R.string.nick_change_hint:
                req.setAccount(content);
                responseToast = R.string.nick_change_success;
                break;

            case R.string.address_change_hint:
                req.setAddress(content);
                responseToast = R.string.address_change_success;
                break;

            case R.string.job_change_hint:
                req.setCareer(content);
                responseToast = R.string.job_change_success;
                break;

            case R.string.industry_change_hint:
                req.setTrade(content);
                responseToast = R.string.industry_change_success;
                break;
        }


        RetrofitFactory
                .getApiService()
                .putUpdatePersonData(req)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<LoginResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onUpdatePersonError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull LoginResp value) {
                        UserCache.save(value.getAccountInfo());
                        EventBus.getDefault().post(new UserInfoUpdateEvent());
                        mView.onUpdatePerson(responseToast);
                    }
                });
    }
}
