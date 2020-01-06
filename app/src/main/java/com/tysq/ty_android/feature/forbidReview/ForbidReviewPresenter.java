package com.tysq.ty_android.feature.forbidReview;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import common.RespData;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import request.ForbidCommentReq;
import request.ForbidReviewReq;
import response.forbid.ForbidReviewBanned;
import response.forbid.ForbidReviewBannedSubBean;
import response.forbid.ForbidReviewBean;
import response.forbid.ForbidReviewResp;
import response.forbid.ForbidReviewSubBean;

/**
 * author       : liaozhenlin
 * time         : 2019-8-27 10:07
 * desc         :
 * version      : 1.0.0
 */
public final class ForbidReviewPresenter extends BasePresenter<ForbidReviewView> {

    private List<ForbidReviewBean> reviewBean = new ArrayList<>();
    private List<ForbidReviewBanned.ForbidReviewBannedBean> bannedBean = new ArrayList<>();

    @Inject
    public ForbidReviewPresenter(ForbidReviewView view) {
        super(view);
    }

    public void getForbidCategory(int uid){
        RetrofitFactory
                .getApiService()
                .getForbidCategory()
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<ForbidReviewResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.onLoadForbidCategoryFailure();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull ForbidReviewResp value) {
                        if (value.getForbidReviewBeanList() == null
                                || value.getForbidReviewBeanList().size() <= 0){
                            return;
                        }
                        getForbidReviewChecked(value, uid);
                    }
                });
    }

    public void postForbidCategory(int accountId, List<ForbidCommentReq> forbidCommentReqs){
        RetrofitFactory
                .getApiService()
                .postForbidComment(new ForbidReviewReq(accountId,
                        forbidCommentReqs))
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        mView.hideDialog();
                        mView.onPostForbidComment();
                    }
                });

    }

    public void getForbidReviewChecked(ForbidReviewResp forbidReviewResp,int uid){

        RetrofitFactory
                .getApiService()
                .getForbidReview(uid)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<ForbidReviewBanned>(mySelf) {

                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code, message);
                        mView.hideDialog();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull ForbidReviewBanned value) {
                        mView.hideDialog();
                        if (value.getForbidReviewBannedBeanList() == null
                                && forbidReviewResp.getForbidReviewBeanList() != null){

                            mView.onGetForbidReview(forbidReviewResp);

                        } else if (forbidReviewResp.getForbidReviewBeanList() == null){
                            return;
                        }else{
                            spliceData(value, forbidReviewResp);
                            mView.onGetForbidReview(forbidReviewResp);
                        }

                    }
                });
    }

    /**
     * 将两个接口请求来的数据进行拼装
     * @param value
     * @param forbidReviewResp
     */
    private void spliceData(@NonNull ForbidReviewBanned value, ForbidReviewResp forbidReviewResp) {

        reviewBean = forbidReviewResp.getForbidReviewBeanList();
        bannedBean = value.getForbidReviewBannedBeanList();

        for (int i = 0 ; i < bannedBean.size(); i++){
            for (int j = 0; j < reviewBean.size(); j++){
                //定位一级板块被勾选
                if (reviewBean.get(j).getCategoryId() == bannedBean.get(i).getCategoryId()){
                    reviewBean.get(j).setChecked(true);
                    setDataAndChecked(i, j);
                }else{
                    //定位一级板块没勾选但二级板块有些被勾选
                    if (bannedBean.get(i).getPositionId() == reviewBean.get(j).getCategoryId()){
                        setDataAndChecked(i,j);
                    }
                }
            }
        }
        forbidReviewResp.setForbidReviewBeanList(reviewBean);
    }

    /**
     * 对按钮进行勾选的设置
     * @param bannedBeanPosition
     * @param reviewBeanPosition
     */
    private void setDataAndChecked(int bannedBeanPosition, int reviewBeanPosition) {

        List<ForbidReviewBannedSubBean> subBanned = bannedBean.get(bannedBeanPosition)
                .getForbidReviewBannedSubBeanList();
        List<ForbidReviewSubBean> subReview = reviewBean.get(reviewBeanPosition)
                .getForbidReviewSubBeanList();

        if (subBanned != null && subReview != null){
            //若两个id相同，则认为已被勾选
            for (int x = 0; x < subBanned.size(); x++){
                for (int k = 0; k < subReview.size(); k++){
                    if (subBanned.get(x).getCategoryId() == subReview.get(k).getCategoryId()){

                        subReview.get(k).setChecked(true);

                    }
                }
                reviewBean.get(reviewBeanPosition).setForbidReviewSubBeanList(subReview);
            }
        }else{
            return;
        }

    }

}
