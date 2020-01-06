package com.tysq.ty_android.feature.forbidList;

import com.bit.view.IView;

import java.util.List;


import response.forbidlist.ForbidCommentResp;

public interface ForbidListView extends IView {
    void onGetForbidListError(boolean isFirst);

    void onGetForbidList(List<ForbidCommentResp.ForbidCommentBean> forbidCommentList, boolean isFirst);
}
