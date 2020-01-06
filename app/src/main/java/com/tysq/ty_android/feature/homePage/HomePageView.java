package com.tysq.ty_android.feature.homePage;

import com.bit.view.IView;

import java.util.List;

import response.AdResp;
import response.home.CategoryResp;

public interface HomePageView extends IView {
    void onLoadCategoryFailure();

    void onLoadCategory(CategoryResp value);

    void onLoadAnnouncement(List<AdResp.AdvertisementListBean> advertisementList);

}
