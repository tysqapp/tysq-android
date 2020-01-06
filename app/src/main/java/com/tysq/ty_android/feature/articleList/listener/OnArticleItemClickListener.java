package com.tysq.ty_android.feature.articleList.listener;

import android.view.View;

import vo.SortVO;

/**
 * author       : frog
 * time         : 2019/4/30 下午3:33
 * desc         : 文章列表点击事件
 * version      : 1.3.0
 */
public interface OnArticleItemClickListener {

    void onArticleItemClick(View view, int position);

    void onSortItemClick(SortVO sortVO);

    void onSearch();

    void onChangePage();

    void onShowPageDialog();

    /**
     * 点击跳转到置顶文章列表
     */
    void onTopArticleList();
}
