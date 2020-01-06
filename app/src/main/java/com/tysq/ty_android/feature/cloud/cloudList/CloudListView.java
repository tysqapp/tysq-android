package com.tysq.ty_android.feature.cloud.cloudList;

import com.bit.view.IView;

import java.util.List;

import response.cloud.FileInfoResp;

public interface CloudListView extends IView {
    void onLoadDataError(boolean isFirst);

    void onLoadData(boolean isFirst,
                    int totalNumber,
                    List<FileInfoResp.FileItem> fileInfo);

    void onDeleteFile(int fileId);

    void onPutRename(int id, String filename);

    void onDownloadError();

    void onDownload();
}
