package com.tysq.ty_android.feature.forbidReview;

import com.bit.view.IView;

import request.ForbidReviewReq;
import response.forbid.ForbidReviewBanned;
import response.forbid.ForbidReviewResp;

/**
 * author       : liaozhenlin
 * time         : 2019-8-27 10:07
 * desc         :
 * version      : 1.0.0
 */
public interface ForbidReviewView extends IView {

    void onLoadForbidCategoryFailure();

    void onPostForbidComment();

    void onGetForbidReview(ForbidReviewResp forbidReviewResp);

}
