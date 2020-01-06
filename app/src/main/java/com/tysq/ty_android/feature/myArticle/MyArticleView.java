package com.tysq.ty_android.feature.myArticle;

import com.bit.view.IView;

import java.util.List;

import response.article.MyArticleResp;

public interface MyArticleView extends IView {
    void onLoadArticleError(boolean isFirst);

    void onLoadArticle(boolean isFirst,
                       List<MyArticleResp.ArticlesInfoBean> articlesInfo,
                       int totalNum);
}
