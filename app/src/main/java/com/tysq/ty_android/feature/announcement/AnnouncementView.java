package com.tysq.ty_android.feature.announcement;

import com.bit.view.IView;

import java.util.List;

import response.AdResp;

public interface AnnouncementView extends IView {
    void onGetAnnouncementError(boolean isFirst);

    void onGetAnnouncement(boolean isFirst,
                           List<AdResp.AdvertisementListBean> advertisementList);
}
