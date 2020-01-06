package com.tysq.ty_android.feature.reportDetail;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tysq.ty_android.R;
import com.tysq.ty_android.base.activity.CommonBarStrengthenActivity;

/**
 * author       : liaozhenlin
 * time         : 2019/9/17 15:40
 * desc         : 举报详情
 * version      : 1.5.0
 */
public class ReportDetailActivity extends CommonBarStrengthenActivity {

    private String mReportId;

    public static void startActivity(Context context, String reportId) {
        Intent intent = new Intent(context, ReportDetailActivity.class);

        intent.putExtra(ReportDetailFragment.REPORT_ID, reportId);

        context.startActivity(intent);
    }

    @Override
    protected void initIntent(Intent intent) {
        mReportId = intent.getStringExtra(ReportDetailFragment.REPORT_ID);
    }

    @Override
    protected int getPageTitle() {
        return R.string.report_detail_title;
    }

    @Override
    protected Fragment getContentFragment() {
        return ReportDetailFragment.newInstance(mReportId);
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
