package com.tysq.ty_android.feature.announcement;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.SimpleLoadMoreFragment;
import com.tysq.ty_android.feature.announcement.di.AnnouncementModule;
import com.tysq.ty_android.feature.announcement.di.DaggerAnnouncementComponent;

import java.util.List;

import response.AdResp;

/**
 * author       : frog
 * time         : 2019-08-20 16:10
 * desc         : 公告
 * version      : 1.3.0
 */

public final class AnnouncementFragment
        extends SimpleLoadMoreFragment<AnnouncementPresenter, AdResp.AdvertisementListBean>
        implements AnnouncementView {

    public static final String TAG = "AnnouncementFragment";

    public static AnnouncementFragment newInstance() {

        Bundle args = new Bundle();

        AnnouncementFragment fragment = new AnnouncementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void registerDagger() {
        DaggerAnnouncementComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .announcementModule(new AnnouncementModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.getAnnouncement(start, pageSize, isFirst);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new AnnouncementAdapter(getContext(), mData);
    }

    @Override
    public int getTitleId() {
        return R.string.announcement;
    }

    @Override
    public void onGetAnnouncementError(boolean isFirst) {
        onHandleError(isFirst);
    }

    @Override
    public void onGetAnnouncement(boolean isFirst,
                                  List<AdResp.AdvertisementListBean> advertisementList) {
        onHandleResponseData(advertisementList, isFirst);
    }
}
