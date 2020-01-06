package com.tysq.ty_android.feature.invite;

import com.bit.view.IView;

import response.InviteResp;

public interface InviteView extends IView {
    void onGetInviteFailure(boolean isFirst);

    void onGetInvite(InviteResp value, boolean isFirst);
}
