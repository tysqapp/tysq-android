package com.tysq.ty_android.feature.myFans;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tysq.ty_android.R;
import com.tysq.ty_android.app.TyApplication;
import com.tysq.ty_android.base.SimpleLoadMoreFragment;
import com.tysq.ty_android.base.activity.CommonToolbarStrengthenActivity;
import com.tysq.ty_android.feature.myFans.adapter.MyFansAdapter;
import com.tysq.ty_android.feature.myFans.di.DaggerMyFansComponent;
import com.tysq.ty_android.feature.myFans.di.MyFansModule;
import com.tysq.ty_android.feature.myFans.listener.MyFansAttentionClickListener;
import com.tysq.ty_android.local.sp.UserCache;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import eventbus.AttentionEvent;
import response.MyFansListResp;
import response.common.TitleCountVO;

public final class MyFansFragment
        extends SimpleLoadMoreFragment<MyFansPresenter, MyFansListResp.AttentionInfoBean>
        implements MyFansView,
        CommonToolbarStrengthenActivity.ICommonFragment,
        MyFansAttentionClickListener {

    public static final String TAG = "MyFansFragment";

    private final TitleCountVO mTitleCountVO = new TitleCountVO();

    public static MyFansFragment newInstance() {
        return newInstance(UserCache.getDefault().getAccountId(),
                true,
                true,
                true);
    }

    public static MyFansFragment newInstance(int userId,
                                             boolean isNeedHeader,
                                             boolean isNeedRefresh,
                                             boolean isNeedSwipe) {
        Bundle args = new Bundle();
        args.putInt(USER_ID, userId);
        args.putBoolean(IS_NEED_HEADER, isNeedHeader);
        args.putBoolean(IS_NEED_REFRESH, isNeedRefresh);
        args.putBoolean(IS_NEED_SWIPE, isNeedSwipe);

        MyFansFragment fragment = new MyFansFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new MyFansAdapter(this, getContext(), mTitleCountVO, mData, this, isNeedHeader);
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        userId = arguments.getInt(USER_ID);
    }

    @Override
    protected void registerDagger() {
        DaggerMyFansComponent.builder()
                .appComponent(TyApplication.getAppComponent())
                .myFansModule(new MyFansModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void loadData(int start, int pageSize, boolean isFirst) {
        mPresenter.getMyFansList(userId, start, pageSize, isFirst);
    }

    @Override
    public int getTitleId() {
        return R.string.my_fans_title;
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
    public void onGetMyFansList(boolean isFirst,
                                int totalNum,
                                List<MyFansListResp.AttentionInfoBean> attentionInfo) {

        mTitleCountVO.setCount(totalNum);
        onHandleResponseData(attentionInfo, isFirst);
    }

    @Override
    public void postAttention() {
    }

    @Override
    public void onMyFansAttentionClick(View view, int attentionId, boolean isFollow) {

        mPresenter.postAttention(attentionId, isFollow);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void attentionChange(AttentionEvent event) {

        int attentionIndex = -1;
        for (int i = 0; i < mData.size(); i++) {
            MyFansListResp.AttentionInfoBean item = mData.get(i);
            if (item.getAccountId() == event.getAccountId()) {
                attentionIndex = i;
                break;
            }
        }

        if (attentionIndex == -1) {
            return;
        }

        mData.get(attentionIndex).setFollow(event.getIsFollow());

        //头部坐标
        int headPos = (isNeedHeader ? 1 : 0);
        //刷新头部坐标
        int refreshPos = (isNeedRefresh ? 1 : 0);
        int realIndex = attentionIndex + headPos + refreshPos;
        mBaseAdapter.notifyItemChanged(realIndex);
    }
}
