package com.tysq.ty_android.feature.topArticleList;

import com.bit.view.IView;

import java.util.List;

import response.TopArticleResp;

public interface TopArticleListView extends IView {

    void onLoadTopArticleList(List<TopArticleResp.TopArticleBean> value);
}
