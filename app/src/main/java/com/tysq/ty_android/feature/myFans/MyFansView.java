package com.tysq.ty_android.feature.myFans;

import com.bit.view.IView;

import java.util.List;

import response.MyAttentionListResp;
import response.MyFansListResp;

public interface MyFansView extends IView {

    void onGetMyFansList(boolean isFirst,
                         int totalNum,
                         List<MyFansListResp.AttentionInfoBean> attentionInfo);

    void postAttention();
}
