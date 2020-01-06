package com.tysq.ty_android.feature.rewardList;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.SimpleLoadMoreStrengthenFragment;
import com.tysq.ty_android.feature.rewardList.di.DaggerRewardListComponent;
import com.tysq.ty_android.feature.rewardList.di.RewardListModule;
import java.lang.Override;

import response.article.RewardListResp;
import vo.RewardListVO;
/**
 * author       : liaozhenlin
 * time         : 2019/11/13 16:45
 * desc         : 打赏文章列表
 * version      : 1.5.0
 */
public final class RewardListFragment extends
        SimpleLoadMoreStrengthenFragment<RewardListPresenter, RewardListResp.RewardListBean>
        implements RewardListView {

    public static final String ARTICLE_ID = "ARTICLE_ID";
    public static final String TAG = "RewardListFragment";

    public final RewardListVO reWardListVO = new RewardListVO();

    private String mArticleId;

    public static RewardListFragment newInstance(String articleId){
        Bundle args = new Bundle();
        args.putString(ARTICLE_ID, articleId);

        RewardListFragment fragment = new RewardListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        mArticleId = arguments.getString(ARTICLE_ID);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new RewardListAdapter(this, getContext(), reWardListVO, mData);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void registerDagger() {
        DaggerRewardListComponent.builder()
        .appComponent(TyApplication.getAppComponent())
        .rewardListModule(new RewardListModule(this))
        .build()
        .inject(this);
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.loadRewardList(mArticleId, pageSize, start, isFirst);
    }

    @Override
    public int getTitleId() {
    return R.string.reward_list_title;
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
    public void onLoadRewardList(RewardListResp value, boolean isFirst) {
        reWardListVO.setCount(value.getTotalNum());
        onHandleResponseData(value.getRewardListBeanList(), isFirst);
    }
}
