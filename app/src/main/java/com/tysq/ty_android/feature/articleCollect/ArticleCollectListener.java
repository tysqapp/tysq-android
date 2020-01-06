package com.tysq.ty_android.feature.articleCollect;

/**
 * author       : frog
 * time         : 2019/6/5 下午6:23
 * desc         : 收藏回调
 * version      : 1.3.0
 */
public interface ArticleCollectListener {

    /**
     * 取消收藏
     *
     * @param articleId 文章的id
     */
    void cancelCollect(String articleId);

}
