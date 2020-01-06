package com.tysq.ty_android.feature.reportDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.articleDetail.activity.ArticleDetailActivity;
import com.tysq.ty_android.feature.reportDetail.di.DaggerReportDetailComponent;
import com.tysq.ty_android.feature.reportDetail.di.ReportDetailModule;
import java.lang.Override;

import butterknife.BindView;
import response.report.ReportDetailResp;

public final class ReportDetailFragment
        extends BitBaseFragment<ReportDetailPresenter>
        implements ReportDetailView {

    public static final String TAG = "ReportDetailFragment";
    public static final String REPORT_ID = "report_id";
    private String mArticleId;
    private String mReportId;

    @BindView(R.id.rl_report_article)
    RelativeLayout rlReportArticle;
    @BindView(R.id.tv_report_article_title)
    TextView tvArticleTitle;
    @BindView(R.id.tv_report_content)
    TextView tvReportContent;
    @BindView(R.id.tv_report_description)
    TextView tvReportDescription;
    @BindView(R.id.tv_report_result)
    TextView tvReportResult;
    @BindView(R.id.ll_handle_result)
    LinearLayout llHandelResult;
    @BindView(R.id.tv_report_result_description)
    TextView tvResultDescription;
    @BindView(R.id.tv_report_num)
    TextView tvReportNum;

    public static ReportDetailFragment newInstance(String reportId){
        Bundle args = new Bundle();
        args.putString(REPORT_ID, reportId);

        ReportDetailFragment fragment = new ReportDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        mReportId = arguments.getString(REPORT_ID);
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_detail, container, false);
    }

    @Override
    protected void initView(View view) {
        mPresenter.getReportDetail(mReportId);
        rlReportArticle.setOnClickListener(view1 -> 
                ArticleDetailActivity.startActivity(getContext(), mArticleId)
        );
    }

    @Override
    protected void registerDagger() {
          DaggerReportDetailComponent.builder()
          .appComponent(TyApplication.getAppComponent())
          .reportDetailModule(new ReportDetailModule(this))
          .build()
          .inject(this);
    }

    @Override
    public void getReportDetail(ReportDetailResp resp) {
        tvArticleTitle.setText(resp.getTitle());
        tvReportContent.setText(resp.getType());
        tvReportDescription.setText(resp.getNote());
        tvReportResult.setText(resp.getResult());
        tvResultDescription.setText(resp.getProcessReason());
        if (resp.getProcessReason().trim().length() <= 0){
            llHandelResult.setVisibility(View.GONE);
        }
        tvReportNum.setText(resp.getReportId());
        mArticleId = resp.getArticleId();
    }
}
