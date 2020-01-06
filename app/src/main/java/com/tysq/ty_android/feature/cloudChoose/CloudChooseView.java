package com.tysq.ty_android.feature.cloudChoose;

import com.bit.view.IView;

import java.util.List;

import response.cloud.FileInfoResp;

public interface CloudChooseView extends IView {
    void onError(boolean isFirst);

    void onLoad(boolean isFirst, List<FileInfoResp.FileItem> fileInfo);

    void onCommitHeadChangeInfo();

    void onCommitCover(String url, int coverId, int fileId);
}
