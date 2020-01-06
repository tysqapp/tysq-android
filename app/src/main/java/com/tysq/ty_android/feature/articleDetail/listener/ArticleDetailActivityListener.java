package com.tysq.ty_android.feature.articleDetail.listener;

import response.permission.PermissionResp;

/**
 * author       : frog
 * time         : 2019/5/23 上午9:52
 * desc         :
 * version      : 1.3.0
 */
public interface ArticleDetailActivityListener {

    /**
     * 设置文章信息，作者名字 和 评论数量
     */
    void onSetArticleInfo(String name, int count);

    /**
     * 设置 评论数量
     */
    void onSetCount(long count);

    /**
     * 设置状态栏标题
     *
     * @param title 标题
     * @param alpha 透明度百分比
     */
    void onSetTitle(String title, float alpha);

    /**
     * 设置收集状态
     *
     * @param state 状态
     */
    void onSetCollectState(boolean state);

    /**
     * 隐藏控制栏
     */
    void onHideControlBar();

    /**
     * 控制板 权限
     */
    void onSetPermission(PermissionResp value);

    /**
     * 获取文章是否隐藏
     */
    void onGetIsHideArticle(int status);
}
