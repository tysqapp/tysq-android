package com.tysq.ty_android.feature.reportDetail;

import com.bit.view.IView;

import response.report.ReportDetailResp;

public interface ReportDetailView extends IView {
    void getReportDetail(ReportDetailResp resp);
}
