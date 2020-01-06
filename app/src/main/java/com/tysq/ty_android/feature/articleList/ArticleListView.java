package com.tysq.ty_android.feature.articleList;

import com.bit.view.IView;

import java.util.List;

import response.AdResp;
import response.TopArticleResp;
import response.home.ArticleInfo;

public interface ArticleListView extends IView {

    void onLoadArticleList(List<ArticleInfo> articleInfoList,
                           List<AdResp.AdvertisementListBean> advertisementList,
                           List<TopArticleResp.TopArticleBean> topArticleList,
                           int topId,
                           int subId);

    void onError();

    void onErrorLoadMore();

    void onLoadMoreArticleList(List<ArticleInfo> articleInfoList);
}
