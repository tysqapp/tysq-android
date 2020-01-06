package com.tysq.ty_android.feature.articleExam;

import com.bit.view.IView;

import java.util.List;

import response.article.ReviewArticleListResp;

public interface ArticleExamView extends IView {

    void onLoadExamArticle(List<ReviewArticleListResp.ReviewArticlesBean> list,
                           int totalNum,
                           boolean isFirst);

    void onLoadExamArticleError(boolean isFirst);

}
