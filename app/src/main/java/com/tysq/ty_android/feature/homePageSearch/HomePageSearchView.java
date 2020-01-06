package com.tysq.ty_android.feature.homePageSearch;

import com.bit.view.IView;

import java.util.List;

import response.LabelResp;
import vo.search.HomePageSearchVO;

public interface HomePageSearchView extends IView {
    void onLoadHomePage( List<HomePageSearchVO> resultList, boolean isRefresh, boolean isNeedAdvertisement, int count);

    void onFirstLoad( List<HomePageSearchVO> resultList);

    void onLoadMoreData(List<HomePageSearchVO> resultList);
}
