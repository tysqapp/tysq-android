package com.tysq.ty_android.feature.articleCollect;

import com.bit.view.IView;

import java.util.List;

import response.article.ArticleCollectResp;

public interface ArticleCollectView extends IView {
    void onGetArticleCollectError(boolean isFirst);

    void onGetArticleCollect(boolean isFirst,
                             List<ArticleCollectResp.CollectsBean> collects,
                             int totalNum);

    void onCancelCollectError(String articleId);

    void onCancelCollect(String articleId);
}
