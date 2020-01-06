package com.tysq.ty_android.feature.articleReport;

import android.view.View;

public interface OnArticleReportItemClickListener extends View.OnClickListener {
    void onArticleReportItemClick(View view, boolean isClick);

    @Override
    void onClick(View view);
}
