package com.tysq.ty_android.feature.review;

import com.bit.view.IView;

import vo.article.ArticleDetailVO;

public interface ReviewView extends IView {

    void onPublishComment(ArticleDetailVO articleDetailVO, int score);

}
