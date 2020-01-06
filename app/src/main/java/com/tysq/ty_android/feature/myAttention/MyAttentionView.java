package com.tysq.ty_android.feature.myAttention;

import com.bit.view.IView;

import java.util.List;

import response.MyAttentionListResp;

public interface MyAttentionView extends IView {

    void onGetMyAttentionListError();
    void onGetMyAttentionList(boolean isFirst,
                              int totalNum,
                              List<MyAttentionListResp.AttentionInfoBean> attentionInfoBean);

    void postAttention();
}
