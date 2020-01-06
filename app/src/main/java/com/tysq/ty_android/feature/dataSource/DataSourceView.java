package com.tysq.ty_android.feature.dataSource;

import com.bit.view.IView;

import response.UpdateResp;

public interface DataSourceView extends IView {
    void onCheckDataSource(String dataSource);

    void onCheckDataSourceError();

    void onGetUpdateInfo(UpdateResp updateResp);

    void onGetUpdateInfoError();

    void onCheckDataSourceInvalidate();
}
