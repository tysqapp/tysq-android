package com.tysq.ty_android.feature.articleReport;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.activity.CommonBarStrengthenActivity;

/**
 * author       : liaozhenlin
 * time         : 2019/9/17 17:19
 * desc         : 文章详情举报
 * version      : 1.5.0
 */
public class ArticleReportActivity extends CommonBarStrengthenActivity {

    public static final String ARTICLE_TITLE = "article_title";
    public static final String ARTICLE_ID = "article_id";

    private String articleTitle;
    private String articleId;

    public static void startActivity(Context context, String articleTitle, String articleId) {
        Intent intent = new Intent(context, ArticleReportActivity.class);
        intent.putExtra(ARTICLE_TITLE, articleTitle);
        intent.putExtra(ARTICLE_ID, articleId);

        context.startActivity(intent);
    }

    @Override
    protected void initIntent(Intent intent) {
        intent = getIntent();
        articleTitle = intent.getStringExtra(ARTICLE_TITLE);
        articleId = intent.getStringExtra(ARTICLE_ID);
    }

    @Override
    protected int getPageTitle() {
        return R.string.article_report;
    }

    @Override
    protected Fragment getContentFragment() {
        return ArticleReportFragment.newInstance(articleTitle, articleId);
    }

    @Override
    protected boolean isGetConfirm() {
        return false;
    }

    @Override
    protected int getConfirmText() {
        return 0;
    }

    @Override
    protected int getConfirmBackground() {
        return 0;
    }

    @Override
    protected int getConfirmTextColor() {
        return 0;
    }
}
