package com.tysq.ty_android.feature.editArticle;

import com.bit.view.IView;

import response.article.ArticleDetailResp;
import response.article.PublishArticleResp;
import response.home.CategoryResp;

public interface EditArticleView extends IView {
    /**
     * 发布新文章成功
     *
     * @param isPublish          是否发布
     *                           false：草稿
     *                           true：发布
     * @param publishArticleResp 文章信息
     */
    void onPublishArticle(boolean isPublish, PublishArticleResp publishArticleResp);

    /**
     * 文章信息加载回调错误
     */
    void onLoadArticleInfoError();

    /**
     * 文章信息加载成功
     *
     * @param articleInfo 文章信息
     */
    void onLoadArticleInfo(ArticleDetailResp.ArticleInfoBean articleInfo, CategoryResp categoryResp);

    /**
     * 更新文章
     *
     * @param resp 文章信息
     * @param isPublish         是否发布
     */
    void onUpdateArticle(PublishArticleResp resp, boolean isPublish);

    /**
     * 积分不够
     */
    void onNotEnoughRank(PublishArticleResp value);

    /**
     * 加载分类
     */
    void onLoadCategory(CategoryResp value);

}
