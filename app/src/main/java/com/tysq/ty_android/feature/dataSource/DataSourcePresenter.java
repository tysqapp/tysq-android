package com.tysq.ty_android.feature.dataSource;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.config.NetConfig;
import com.tysq.ty_android.net.rx.RxObservableSubscriber;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;
import com.tysq.ty_android.utils.DBManager.DBLocalDataSourceManager;
import com.tysq.ty_android.utils.DataSourceChangeUtils;
import com.tysq.ty_android.utils.TyUtils;

import javax.inject.Inject;

import exception.ParserUrlException;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import response.UpdateResp;

public final class DataSourcePresenter extends BasePresenter<DataSourceView> {
    @Inject
    public DataSourcePresenter(DataSourceView view) {
        super(view);
    }

    /**
     * 检测数据源
     */
    public void checkDataSource(String dataSource) {

        try {
            dataSource = TyUtils.getRealDataSource(dataSource);
        } catch (ParserUrlException e) {
            e.printStackTrace();
            mView.onCheckDataSourceInvalidate();
            return;
        }

        DBLocalDataSourceManager.addLocalHistory(dataSource);

        final String finalDataSource = dataSource;

        mView.showDialog();

        Log.i(TAG, "checkDataSource: " + finalDataSource);

        RetrofitFactory
                .getApiService(dataSource)
                .checkDataSource(NetConfig.getBaseUrl(dataSource)
                        + NetConfig.CHECK_DATA_SOURCE_URL)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        DataSourceChangeUtils.cleanNet();

                        mView.hideDialog();
                        mView.onCheckDataSourceError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        DataSourceChangeUtils.initNet(finalDataSource);
                        DataSourceChangeUtils.initHtml();

                        mView.hideDialog();
                        mView.onCheckDataSource(finalDataSource);
                    }
                });

    }

    public void getUpdateInfo() {

        mView.showDialog();

        RetrofitFactory
                .getApiService()
                .getUpdateInfo(NetConfig.getUpdateUrl())
                .doOnNext(updateResp -> {
                    int type = TyUtils.checkUpdateInfo(updateResp.getMinimumSupportVersion(),
                            updateResp.getLatestVersion());
                    updateResp.setUpdateType(type);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<UpdateResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        Log.i(TAG, "[code: " + code + "; " +
                                "message: " + message + "]");
                        mView.hideDialog();
                        mView.onGetUpdateInfoError();
                    }

                    @Override
                    protected void onSuccessRes(UpdateResp value) {
                        mView.hideDialog();
                        mView.onGetUpdateInfo(value);
                    }
                });

    }

}
