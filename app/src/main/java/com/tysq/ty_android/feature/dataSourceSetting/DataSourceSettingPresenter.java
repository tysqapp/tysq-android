package com.tysq.ty_android.feature.dataSourceSetting;

import android.support.annotation.NonNull;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.config.NetConfig;
import com.tysq.ty_android.net.rx.RxParser;
import com.tysq.ty_android.net.rx.RxSingleSubscriber;
import com.tysq.ty_android.utils.DataSourceChangeUtils;

import javax.inject.Inject;

public final class DataSourceSettingPresenter extends BasePresenter<DataSourceSettingView> {
    @Inject
    public DataSourceSettingPresenter(DataSourceSettingView view) {
        super(view);
    }

    public void checkDataSource(String dataSource) throws Exception {

        RetrofitFactory
                .getApiService()
                .checkDataSource(NetConfig.getBaseUrl(dataSource)
                        + NetConfig.CHECK_DATA_SOURCE_URL)
                .compose(RxParser.handleSingleDataResult())
                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onCheckDataSourceError();
                    }

                    @Override
                    protected void onSuccessRes(@NonNull Object value) {
                        DataSourceChangeUtils.initNet(dataSource);

                        mView.onCheckDataSource();
                    }
                });

    }
}
