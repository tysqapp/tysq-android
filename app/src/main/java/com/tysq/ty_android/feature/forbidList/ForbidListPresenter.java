package com.tysq.ty_android.feature.forbidList;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import javax.inject.Inject;

import response.forbidlist.ForbidCommentResp;

public final class ForbidListPresenter extends BasePresenter<ForbidListView> {
   @Inject
    public ForbidListPresenter(ForbidListView view) {
    super(view);
  }


    public void getForbidList(int start, int size,String account, boolean isFirst){
      RetrofitFactory
              .getApiService()
              .getForbidComment(start, size, account)
              .compose(RxParser.handleSingleDataResult())
              .subscribe(new RxSingleSubscriber<ForbidCommentResp>(mySelf) {
                @Override
                protected void onError(int code, String message) {
                    showErrorMsg(code, message);
                    mView.onGetForbidListError(isFirst);
                }

                  @Override
                  protected void onSuccessRes(@NonNull ForbidCommentResp value) {
                      mView.onGetForbidList(value.getForbidCommentBean(),isFirst);
                  }
              });
    }
}
