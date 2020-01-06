package com.tysq.ty_android.feature.cloud.cloudDownload;

import com.bit.view.IView;

import java.util.List;

import vo.cloud.CloudDownloadVO;

public interface CloudDownloadView extends IView {
    void onLoadError();

    void onLoadInfo(List<CloudDownloadVO> value, int count);
}
