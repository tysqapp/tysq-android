package com.tysq.ty_android.feature.adminCenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.activity.CommonToolbarActivity;
import com.tysq.ty_android.feature.adminCenter.di.AdminCenterModule;
import com.tysq.ty_android.feature.adminCenter.di.DaggerAdminCenterComponent;
import com.tysq.ty_android.feature.articleExam.ArticleExamFragment;
import com.tysq.ty_android.feature.forbidList.ForbidListActivity;


import java.lang.Override;

import butterknife.BindView;

/**
 * author       : xxxx
 * time         : 2019-08-28 11:01
 * desc         : 版主中心
 * version      : 1.0.0
 */

public final class AdminCenterFragment
        extends BitBaseFragment<AdminCenterPresenter>
        implements AdminCenterView, View.OnClickListener, CommonToolbarActivity.ICommonFragment {

    public static final String TAG = "AdminCenterFragment";

    @BindView(R.id.ll_exam_article)
    LinearLayout llExamArticle;
    @BindView(R.id.tv_exam_article)
    TextView tvExamArticle;
    @BindView(R.id.ll_forbid_comment)
    LinearLayout llForbidComment;

    public static AdminCenterFragment newInstance() {

        Bundle args = new Bundle();

        AdminCenterFragment fragment = new AdminCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        ViewGroup container,
                                        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_center, container, false);
    }

    @Override
    protected void initView(View view) {
        llExamArticle.setOnClickListener(this);
        llForbidComment.setOnClickListener(this);
    }

    @Override
    protected void registerDagger() {
        DaggerAdminCenterComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .adminCenterModule(new AdminCenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadExamArticleInfo();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_exam_article:
                CommonToolbarActivity.startActivity(getContext(), ArticleExamFragment.TAG);
                break;
            case R.id.ll_forbid_comment:
                ForbidListActivity.startActivity(getContext());
                break;
        }
    }

    @Override
    public void onLoadExamArticleInfo(String total) {
        tvExamArticle.setText(total);

    }

    @Override
    public int getTitleId() {
        return R.string.admin_center;
    }

}
