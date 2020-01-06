package com.tysq.ty_android.feature.myAttention;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.SimpleLoadMoreFragment;
import com.tysq.ty_android.base.activity.CommonToolbarStrengthenActivity;
import com.tysq.ty_android.feature.myAttention.adapter.MyAttentionAdapter;
import com.tysq.ty_android.feature.myAttention.di.DaggerMyAttentionComponent;
import com.tysq.ty_android.feature.myAttention.di.MyAttentionModule;
import com.tysq.ty_android.feature.myAttention.listener.MyAttentionClickListener;
import com.tysq.ty_android.local.sp.UserCache;

import java.util.List;

import response.MyAttentionListResp;
import response.common.TitleCountVO;

public final class MyAttentionFragment
        extends SimpleLoadMoreFragment<MyAttentionPresenter, MyAttentionListResp.AttentionInfoBean>
        implements MyAttentionView,
        CommonToolbarStrengthenActivity.ICommonFragment,
        MyAttentionClickListener {

    public static final String TAG = "MyAttentionFragment";
    private final TitleCountVO mTitleCountVO = new TitleCountVO();

    public static MyAttentionFragment newInstance() {
        return newInstance(UserCache.getDefault().getAccountId(),
                true,
                true,
                true);
    }

    public static MyAttentionFragment newInstance(int userId,
                                                  boolean isNeedHeader,
                                                  boolean isNeedRefresh,
                                                  boolean isNeedSwipe) {
        Bundle args = new Bundle();
        args.putInt(USER_ID, userId);
        args.putBoolean(IS_NEED_HEADER, isNeedHeader);
        args.putBoolean(IS_NEED_REFRESH, isNeedRefresh);
        args.putBoolean(IS_NEED_SWIPE, isNeedSwipe);

        MyAttentionFragment fragment = new MyAttentionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        userId = arguments.getInt(USER_ID);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new MyAttentionAdapter(this, getContext(), mTitleCountVO, mData, this, isNeedHeader, userId);
    }

    @Override
    protected void registerDagger() {
        DaggerMyAttentionComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .myAttentionModule(new MyAttentionModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onGetMyAttentionListError() {

    }

    @Override
    public void onGetMyAttentionList(boolean isFirst,
                                     int totalNum,
                                     List<MyAttentionListResp.AttentionInfoBean> attentionInfoBean) {

        mTitleCountVO.setCount(totalNum);
        onHandleResponseData(attentionInfoBean, isFirst);
    }

    @Override
    public void postAttention() {
    }

    @Override
    protected int getEmptyView() {
        return R.layout.blank_empty_data_log;
    }

    @Override
    protected boolean isNeedEmpty() {
        return true;
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.getMyAttentionList(userId, start, pageSize, isFirst);
    }

    @Override
    public int getTitleId() {
        return R.string.my_attention_title;
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
    public void onMyAttentionClick(View view, int attentionId, boolean isFollow) {
        mPresenter.postAttention(attentionId, isFollow);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(0, 20, true);
    }
}
