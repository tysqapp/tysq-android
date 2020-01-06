package com.tysq.ty_android.feature.articleReport;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.abc.lib_utils.toast.ToastUtils;
import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.feature.articleReport.di.ArticleReportModule;
import com.tysq.ty_android.feature.articleReport.di.DaggerArticleReportComponent;
import com.tysq.ty_android.utils.TyUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import response.report.ArticleReportResp;

/**
 * author       : liaozhenlin
 * time         : 2019/9/17 15:57
 * desc         : 文章详情举报
 * version      : 1.5.0
 */
public final class ArticleReportFragment
        extends BitBaseFragment<ArticleReportPresenter>
        implements ArticleReportView,
        View.OnClickListener,
        TextWatcher,
        OnArticleReportItemClickListener {

    public static final String TAG = "ArticleReportFragment";
    public static final String ARTICLE_TITLE = "account_name";
    public static final String ARTICLE_ID = "article_id";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.btn_report)
    Button btnReport;

    private List<ArticleReportResp> articleReportResps = new ArrayList<>();
    private ArticleReportAdapter adapter;
    private String articleTitle;
    private String articleId;
    private String reportType;


    public static ArticleReportFragment newInstance(String articleTitle, String articleId){
        Bundle args = new Bundle();

        args.putString(ARTICLE_TITLE, articleTitle);
        args.putString(ARTICLE_ID, articleId);

        ArticleReportFragment fragment = new ArticleReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        articleTitle = arguments.getString(ARTICLE_TITLE);
        articleId = arguments.getString(ARTICLE_ID);
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_article_report, container, false);
    }

    @Override
    protected void initView(View view) {
        String name = String.format(getString(R.string.article_report_person), articleTitle);
        tvTitle.setText(name);
        initReportType();

        adapter = new ArticleReportAdapter(getContext(), articleReportResps, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        etDescription.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        etDescription.setSingleLine(false);
        etDescription.setHorizontallyScrolling(false);
        etDescription.addTextChangedListener(this);

        btnReport.setOnClickListener(this);
    }

    private void initReportType() {
        for (int i = 0; i < TyUtils.REPORT_TYPE.length; i++){
            ArticleReportResp articleReport = new ArticleReportResp(TyUtils.REPORT_TYPE[i],false);
            articleReportResps.add(articleReport);
        }
    }


    @Override
    protected void registerDagger() {
        DaggerArticleReportComponent.builder()
        .appComponent(TyApplication.getAppComponent())
        .articleReportModule(new ArticleReportModule(this))
        .build()
        .inject(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_report:
                reportType = adapter.getReportType();
                if (reportType != null){
                    mPresenter.postArticleReport(articleId,
                            reportType,
                            etDescription.getText().toString());
                }
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String content = etDescription.getText().toString().trim();

        //判断是否显示按钮为可点击
        if (content.length() > 0 && adapter.getReportType() != null){
            btnReport.setBackgroundResource(R.drawable.selector_btn_blue_bg);
            btnReport.setClickable(true);
        }else{
            btnReport.setBackgroundColor(getResources().getColor(R.color.shadow_black_color));
            btnReport.setClickable(false);
        }
        if (content.length() > 50){
            String newStr = content.substring(0,50);
            etDescription.setText(newStr);
            Selection.setSelection(etDescription.getText(), 50);
            ToastUtils.show(getString(R.string.resume_change_num_max));
            content = newStr;
        }
        int num = 50 - content.length();
        tvNum.setText(""+ num);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onPostArticleReport() {
        if(getActivity()!=null){
            getActivity().finish();
        }
        ToastUtils.show("举报成功");
    }

    @Override
    public void onArticleReportItemClick(View view, boolean isClick) {
        if (isClick && etDescription.getText().toString().trim().length() > 0){
            btnReport.setBackgroundResource(R.drawable.selector_btn_blue_bg);
            btnReport.setClickable(true);
        }else {
            btnReport.setBackgroundColor(getResources().getColor(R.color.shadow_black_color));
            btnReport.setClickable(false);
        }
    }

}
