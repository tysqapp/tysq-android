package com.tysq.ty_android.feature.topArticleList;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bit.view.fragment.BitBaseFragment;
import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.SimpleLoadMoreStrengthenFragment;
import com.tysq.ty_android.config.TyConfig;
import com.tysq.ty_android.feature.topArticleList.di.DaggerTopArticleListComponent;
import com.tysq.ty_android.feature.topArticleList.di.TopArticleListModule;
import java.lang.Override;
import java.util.List;

import response.TopArticleResp;

public final class TopArticleListFragment
        extends SimpleLoadMoreStrengthenFragment<TopArticleListPresenter, TopArticleResp.TopArticleBean>
        implements TopArticleListView {

    private int mSelTopId;
    private int mSelSubId;

    public static TopArticleListFragment newInstance(int topId, int subId){
        Bundle args = new Bundle();
        args.putInt(TyConfig.TOP_CATEGORY, topId);
        args.putInt(TyConfig.SUB_CATEGORY, subId);

        TopArticleListFragment fragment = new TopArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        mSelTopId = arguments.getInt(TyConfig.TOP_CATEGORY);
        mSelSubId = arguments.getInt(TyConfig.SUB_CATEGORY);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
      return new TopArticleListAdapter(getContext(), mData);
    }

    @Override
    protected void registerDagger() {
        DaggerTopArticleListComponent.builder()
          .appComponent(TyApplication.getAppComponent())
          .topArticleListModule(new TopArticleListModule(this))
          .build()
          .inject(this);
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.getTopArticleList(mSelTopId,mSelSubId);
    }

    @Override
    public int getTitleId() {
      return R.string.top_article;
    }

    @Override
    public int getConfirmId() {
      return 0;
    }

    @Override
    public void setConfirmClick() {

    }

    @Override
    public int getConfirmBackground() {
      return 0;
    }

    @Override
    public int getConfirmTextColor() {
      return 0;
    }

    @Override
    public void onLoadTopArticleList(List<TopArticleResp.TopArticleBean> value) {
        onHandleResponseData(value, true);
    }
}
