package com.tysq.ty_android.feature.reviewDetail.activity;

import com.bit.view.IView;

import response.rank.JudgementResp;

public interface ReviewDetailView extends IView {
    void onGetJudgement(JudgementResp value);
}
