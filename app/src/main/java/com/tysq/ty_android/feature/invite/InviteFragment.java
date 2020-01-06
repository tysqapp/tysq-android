package com.tysq.ty_android.feature.invite;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.config.Constant;
import com.tysq.ty_android.feature.invite.di.DaggerInviteComponent;
import com.tysq.ty_android.feature.invite.di.InviteModule;
import com.tysq.ty_android.base.SimpleLoadMoreFragment;

import java.lang.Override;

import response.InviteResp;

/**
 * author       : frog
 * time         : 2019-07-19 11:27
 * desc         : 邀请
 * version      : 1.3.0
 */
public final class InviteFragment
        extends SimpleLoadMoreFragment<InvitePresenter, InviteResp.ReferralListBean>
        implements InviteView {

    public static final String TAG = "InviteFragment";

    private final InviteResp inviteResp = new InviteResp();

    public static InviteFragment newInstance() {

        Bundle args = new Bundle();

        InviteFragment fragment = new InviteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void registerDagger() {
        DaggerInviteComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .inviteModule(new InviteModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.getInvite(start, pageSize, isFirst);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new InviteAdapter(this, getContext(), inviteResp, mData);
    }

    @Override
    public void onGetInviteFailure(boolean isFirst) {
        onHandleError(isFirst);
    }

    @Override
    public void onGetInvite(InviteResp value, boolean isFirst) {

        inviteResp.setDomainName(value.getDomainName());
        inviteResp.setReferralCode(value.getReferralCode());

        // 拼接分享链接
        String referralLink = value.getReferralLink();
        referralLink += "?type=" + Constant.RegisterType.SCAN;
        referralLink += "&client_type=" + Constant.ClientType.ANDROID;

        inviteResp.setReferralLink(referralLink);
        inviteResp.setTotalNum(value.getTotalNum());
        inviteResp.setScoreNumber(value.getScoreNumber());

        onHandleResponseData(value.getReferralList(), isFirst);
    }

    @Override
    public int getTitleId() {
        return R.string.invite;
    }

    public void showQrCodeFragment() {
        InviteDialog.newInstance(inviteResp.getReferralLink()).show(this);
    }
}
